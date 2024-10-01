package fotcamp.finhub.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.MemberAgreement;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.RefreshToken;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.config.AppleJwtConfig;
import fotcamp.finhub.main.config.GoogleConfig;
import fotcamp.finhub.main.config.KakaoConfig;
import fotcamp.finhub.main.config.OAuth2Util;
import fotcamp.finhub.main.dto.process.login.KakaoUserInfoProcessDto;
import fotcamp.finhub.main.dto.process.login.UserInfoProcessDto;
import fotcamp.finhub.main.dto.request.SignUpAgreeRequest;
import fotcamp.finhub.main.dto.response.login.LoginResponseDto;
import fotcamp.finhub.main.dto.response.login.MemberInfoResponseDto;
import fotcamp.finhub.main.dto.response.login.UpdateAccessTokenResponseDto;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.main.repository.AgreementRepository;
import fotcamp.finhub.main.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService2 {
    private final JwtUtil jwtUtil;

    private final AwsS3Service awsS3Service;
    private final OAuth2Util oAuth2Util;
    private final AuthProviderClient authProviderClient;
    private final KakaoConfig kakaoConfig;
    private final GoogleConfig googleConfig;
    private final AppleJwtConfig appleConfig;

    private final AgreementRepository agreementRepository;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public LoginResponseDto loginKakao(String code, String origin) throws JsonProcessingException {
        String kakaoAccessToken = authProviderClient.getKakaoAccessToken(code, origin);
        KakaoUserInfoProcessDto kakaoUserInfo = authProviderClient.getKakaoUserInfo(kakaoAccessToken);
        String email = kakaoUserInfo.getEmail();
        String name = kakaoUserInfo.getName();
        String uuid = kakaoUserInfo.getUuid(); // 카카오 고유식별자 값
        String provider = kakaoConfig.getClient_name();
        boolean isMember = true; // 기존 유저가 자주 조회할 것으로 예상하여 디폴트는 true
        Member member = memberRepository.findByMemberUuid(uuid)
                .orElseGet(() -> memberRepository.save(new Member(email, name, provider, uuid))); // email, name은 null이어도 무관
        if(!Objects.equals(member.getMemberUuid(), uuid)){ // 제공사 고유식별자 값이 존재하지 않았거나 수정된 경우, 최신 uuid 업데이트
            member.updateMemberUuid(uuid);
        }
        if (!agreementRepository.existsByMemberAndPrivacyPolicyTrueAndTermsOfServiceTrue(member)){ // 멤버 생성은 되어도 약관 동의가 안되면 false 리턴
            isMember = false; // 신규회원
        }
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberUuid(), member.getRole().toString(), member.getProvider());
        saveOrUpdateRefreshToken(member, allTokens.getRefreshToken());
        // 응답 데이터 생성: 닉네임, 이메일, 유저아바타 이미지, 직업명, 직업아바타이미지, 푸시알림 정보, 기존회원유무
        UserInfoProcessDto userInfoProcessDto = createUserInfoProcessDto(member, isMember);
        return new LoginResponseDto(allTokens, userInfoProcessDto);
    }

    private UserInfoProcessDto createUserInfoProcessDto(Member member, boolean isMember) {
        return UserInfoProcessDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .avatarUrl(member.getUserAvatar() != null ? awsS3Service.combineWithCloudFrontBaseUrl(member.getUserAvatar().getAvatar_img_path()) : null)
                .userType(member.getUserType() != null ? member.getUserType().getName() : null)
                .userTypeUrl(member.getUserType() != null ? awsS3Service.combineWithCloudFrontBaseUrl(member.getUserType().getAvatarImgPath()) : null)
                .pushYN(member.getMemberAgreement().isPushYn())
                .isMember(isMember)
                .build();
    }

    public LoginResponseDto loginGoogle(String code, String origin) throws JsonProcessingException {
        String googleAccessToken = authProviderClient.getGoogleAccessToken(code, origin);
        Map<String, Object> userInfo = authProviderClient.getUserInfo(googleAccessToken);
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String sub = (String) userInfo.get("sub");
        String provider = googleConfig.getClient_name();
        boolean isMember = true;
        Member member = memberRepository.findByMemberUuid(sub)
                .orElseGet(() -> memberRepository.save(new Member(email, name, provider, sub)));
        if(!Objects.equals(member.getMemberUuid(), sub)){ // 제공사 고유식별자 값이 존재하지 않았거나 수정된 경우, 최신 uuid 업데이트
            member.updateMemberUuid(sub);
        }
        if(!agreementRepository.existsByMemberAndPrivacyPolicyTrueAndTermsOfServiceTrue(member)){
            isMember = false;
        }
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberUuid(), member.getRole().toString(), provider);
        saveOrUpdateRefreshToken(member, allTokens.getRefreshToken());
        UserInfoProcessDto userInfoProcessDto = createUserInfoProcessDto(member, isMember);
        return new LoginResponseDto(allTokens, userInfoProcessDto);
    }

    public LoginResponseDto loginApple(String code, String origin) throws ParseException, IOException, JOSEException {
        String identityToken = authProviderClient.getAppleIdentityToken(code, origin);
        if (!validateAppleIdToken(identityToken)) {
            throw new RuntimeException();
        }
        SignedJWT signedJWT = SignedJWT.parse(identityToken);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        String email = claims.getStringClaim("email");
        String name = claims.getStringClaim("name");
        String sub = claims.getSubject();
        String provider = appleConfig.getClient_name();
        boolean isMember = true;
        Member member = memberRepository.findByMemberUuid(sub)
                .orElseGet(() -> memberRepository.save(new Member(email, name, provider, sub)));
        if(!Objects.equals(member.getMemberUuid(), sub)){ // 제공사 고유식별자 값이 존재하지 않았거나 수정된 경우, 최신 uuid 업데이트
            member.updateMemberUuid(sub);
        }
        if(!agreementRepository.existsByMemberAndPrivacyPolicyTrueAndTermsOfServiceTrue(member)){
            isMember = false;
        }
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberUuid(), member.getRole().toString(), provider);
        saveOrUpdateRefreshToken(member, allTokens.getRefreshToken());
        UserInfoProcessDto userInfoProcessDto = createUserInfoProcessDto(member, isMember);
        return new LoginResponseDto(allTokens, userInfoProcessDto);
    }

    private boolean validateAppleIdToken(String idToken) throws ParseException, IOException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(idToken);
        // Apple의 공개 키를 가져옴
        JWKSet jwkSet = JWKSet.load(new URL(appleConfig.getPublic_key_url()));
        JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());
        // 검증을 위한 RSA 공개 키 생성
        RSAKey rsaKey = (RSAKey) jwk;
        JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
        // JWT 서명 검증
        return signedJWT.verify(verifier);
    }

    private void saveOrUpdateRefreshToken(Member member, String refreshToken) {
        Optional<RefreshToken> existingRefreshToken = tokenRepository.findByMember(member);
        if (existingRefreshToken.isPresent()) {
            RefreshToken token = existingRefreshToken.get();
            token.updateToken(refreshToken);
            tokenRepository.save(token);
        } else {
            tokenRepository.save(new RefreshToken(member, refreshToken));
        }
    }

    public ResponseEntity<ApiResponseWrapper> validRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("refreshToken");
        if(refreshToken!= null && jwtUtil.validateToken(refreshToken)){
            String uuid = jwtUtil.getUuid(refreshToken);
            String  roleType = jwtUtil.getRoleType(refreshToken);
            String provider = jwtUtil.getProvider(refreshToken);
            String newAccessToken = jwtUtil.createToken(uuid, roleType,"Access", provider);
            UpdateAccessTokenResponseDto updateAccessTokenResponseDto = new UpdateAccessTokenResponseDto(newAccessToken);
            return ResponseEntity.ok(ApiResponseWrapper.success(updateAccessTokenResponseDto));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("액세스토큰 갱신에 실패했습니다."));
        }
    }

    /**
     * 자동로그인
     * */
    public LoginResponseDto autoLogin(HttpServletRequest request){
        String accessTokenHeader = request.getHeader("Authorization");
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = null;
        if (accessTokenHeader != null && accessTokenHeader.startsWith("Bearer ")) {
            accessToken = accessTokenHeader.substring(7); // "Bearer " 이후의 JWT만 추출
        }
        if(jwtUtil.validateTokenServiceLayer(accessToken)){
            // 액세스토큰 유효할 때
            String uuid = jwtUtil.getUuid(accessToken);
            Member member = memberRepository.findByMemberUuid(uuid).orElseThrow(
                    () -> new EntityNotFoundException("MEMBER ID가 존재하지 않습니다."));
            return updatingLoginResponse(member);
        }
        if (jwtUtil.validateTokenServiceLayer(refreshToken)) {
            // 액세스토큰 유효x, 리프레시토큰 유효할 때
            String uuid = jwtUtil.getUuid(refreshToken);
            Member member = memberRepository.findByMemberUuid(uuid).orElseThrow(
                    () -> new EntityNotFoundException("MEMBER ID가 존재하지 않습니다."));
            return updatingLoginResponse(member);
        }
        throw new RuntimeException("로그인이 필요합니다");
    }

    public LoginResponseDto updatingLoginResponse(Member member){
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberUuid(), member.getRole().toString(), member.getProvider());
        Optional<RefreshToken> existingRefreshToken = tokenRepository.findByMember(member);
        if (existingRefreshToken.isPresent()) {
            // 기존 리프레시 토큰 정보가 있는 경우, 새 리프레시 토큰으로 업데이트
            RefreshToken refreshToken = existingRefreshToken.get();
            refreshToken.updateToken(allTokens.getRefreshToken());
            tokenRepository.save(refreshToken);
        } else {
            // 기존 리프레시 토큰 정보가 없는 경우, 새로운 리프레시 토큰 저장
            tokenRepository.save(new RefreshToken(member, allTokens.getRefreshToken()));
        }
        // 응답 데이터 : 닉네임, 이메일, 유저아바타 이미지, 직업명, 직업아바타이미지, 푸시알림 정보
        UserInfoProcessDto userInfoProcessDto = UserInfoProcessDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .avatarUrl(member.getUserAvatar() != null ? awsS3Service.combineWithCloudFrontBaseUrl(member.getUserAvatar().getAvatar_img_path()) : null)
                .userType(member.getUserType() != null ? member.getUserType().getName() : null)
                .userTypeUrl(member.getUserType() != null ? awsS3Service.combineWithCloudFrontBaseUrl(member.getUserType().getAvatarImgPath()) : null)
                .pushYN(member.getMemberAgreement().isPushYn())
                .build();
        return new LoginResponseDto(allTokens, userInfoProcessDto);
    }

    public ResponseEntity<ApiResponseWrapper> memberInfoRequest(CustomUserDetails userDetails){
        if (userDetails == null){
            throw new EntityNotFoundException("잘못된 요청입니다.");
        }
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        UserInfoProcessDto userInfoProcessDto = UserInfoProcessDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .avatarUrl(member.getUserAvatar() != null ? awsS3Service.combineWithCloudFrontBaseUrl(member.getUserAvatar().getAvatar_img_path()) : null)
                .userType(member.getUserType() != null ? member.getUserType().getName() : null)
                .userTypeUrl(member.getUserType() != null ? awsS3Service.combineWithCloudFrontBaseUrl(member.getUserType().getAvatarImgPath()) : null)
                .pushYN(member.getMemberAgreement().isPushYn())
                .build();
        return ResponseEntity.ok(ApiResponseWrapper.success(new MemberInfoResponseDto(userInfoProcessDto)));
    }

    public ResponseEntity<ApiResponseWrapper> signUpAgree(SignUpAgreeRequest signUpAgreeRequest, CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findMemberWithAgreement(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        member.getMemberAgreement().agreeServiceTerms(signUpAgreeRequest.privacy_policy(), signUpAgreeRequest.terms_of_service());
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }
}
