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
import fotcamp.finhub.common.domain.Agreement;
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
import fotcamp.finhub.main.repository.SignUpAgreementRepository;
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

    private final KakaoConfig kakaoConfig;
    private final GoogleConfig googleConfig;
    private final AppleJwtConfig appleConfig;
    private final AwsS3Service awsS3Service;
    private final OAuth2Util oAuth2Util;

    private final SignUpAgreementRepository signUpAgreementRepository;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public ResponseEntity<ApiResponseWrapper> loginKakao(String code, String origin) throws JsonProcessingException {
        String kakaoAccessToken = getKakaoAccessToken(code, origin);
        KakaoUserInfoProcessDto kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);
        String email = kakaoUserInfo.getEmail();
        String name = kakaoUserInfo.getName();
        String uuid = kakaoUserInfo.getUuid(); // 카카오 고유식별자 값
        String provider = kakaoConfig.getClient_name();
        AtomicBoolean isMember = new AtomicBoolean(true); // AtomicBoolean을 사용하여 isMember를 람다 내부에서 수정할 수 있도록 설정 ( 기존 유저가 자주 조회할 것으로 예상하여 디폴트는 true)
        Member member = memberRepository.findByEmailAndProvider(email, provider)
                .orElseGet(() -> {
                    isMember.set(false); // 신규회원
                    return memberRepository.save(new Member(email, name, provider, uuid)); // email, name은 null이어도 무관
                });
        if(!Objects.equals(member.getMemberUuid(), uuid)){ // 제공사 고유식별자 값이 존재하지 않았거나 수정된 경우, 최신 uuid 업데이트
            member.updateMemberUuid(uuid);
        }
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberUuid(), member.getRole().toString(), member.getProvider());
        saveOrUpdateRefreshToken(member, allTokens.getRefreshToken());
        // 응답 데이터 생성: 닉네임, 이메일, 유저아바타 이미지, 직업명, 직업아바타이미지, 푸시알림 정보, 기존회원유무
        UserInfoProcessDto userInfoProcessDto = createUserInfoProcessDto(member, isMember);
        LoginResponseDto loginResponseDto = new LoginResponseDto(allTokens, userInfoProcessDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
    }

    private String getKakaoAccessToken(String code, String origin) throws JsonProcessingException {
        String redirectUri = getKakaoRedirectUri(origin);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", kakaoConfig.getGrant_type());
        body.put("client_id", kakaoConfig.getClientId());
        body.put("redirect_uri", redirectUri);
        body.put("code", code);
        body.put("client_secret", kakaoConfig.getClient_secretId());
        return oAuth2Util.getAccessToken(kakaoConfig.getAccessTokenRequestUrl(), headers, body);
    }

    private String getKakaoRedirectUri(String origin) {
        switch (origin) {
            case "production":
                return kakaoConfig.getRedirect_uri_feProd();
            case "belocal":
                return kakaoConfig.getRedirect_uri_beLocal();
            case "beprod":
                return kakaoConfig.getRedirect_uri_beProd();
            default:
                return kakaoConfig.getRedirect_uri_feLocal();
        }
    }

    private KakaoUserInfoProcessDto getKakaoUserInfo(String accessToken) throws JsonProcessingException  {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoConfig.getUser_info_uri(),
                HttpMethod.POST,
                request,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String id = jsonNode.get("id").asText(); // 카카오 고유식별자
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        return new KakaoUserInfoProcessDto(nickname, email, id);
    }

    private UserInfoProcessDto createUserInfoProcessDto(Member member, AtomicBoolean isMember) {
        return UserInfoProcessDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .avatarUrl(member.getUserAvatar() != null ? awsS3Service.combineWithCloudFrontBaseUrl(member.getUserAvatar().getAvatar_img_path()) : null)
                .userType(member.getUserType() != null ? member.getUserType().getName() : null)
                .userTypeUrl(member.getUserType() != null ? awsS3Service.combineWithCloudFrontBaseUrl(member.getUserType().getAvatarImgPath()) : null)
                .pushYN(member.isPushYn())
                .isMember(isMember.get())
                .build();
    }

    public ResponseEntity<ApiResponseWrapper> loginGoogle(String code, String origin) throws JsonProcessingException {
        String googleAccessToken = getGoogleAccessToken(code, origin);
        Map<String, Object> userInfo = oAuth2Util.getUserInfo(googleConfig.getUser_info_uri(), googleAccessToken);
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String sub = (String) userInfo.get("sub");
        String provider = googleConfig.getClient_name();
        AtomicBoolean isMember = new AtomicBoolean(true);
        Member member = memberRepository.findByEmailAndProvider(email, provider)
                .orElseGet(() -> {
                    isMember.set(false); // 신규회원
                    return memberRepository.save(new Member(email, name, provider, sub));
                });
        if(!Objects.equals(member.getMemberUuid(), sub)){ // 제공사 고유식별자 값이 존재하지 않았거나 수정된 경우, 최신 uuid 업데이트
            member.updateMemberUuid(sub);
        }
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberUuid(), member.getRole().toString(), provider);
        saveOrUpdateRefreshToken(member, allTokens.getRefreshToken());
        UserInfoProcessDto userInfoProcessDto = createUserInfoProcessDto(member, isMember);
        LoginResponseDto loginResponseDto = new LoginResponseDto(allTokens, userInfoProcessDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
    }

    private String getGoogleAccessToken(String code, String origin) throws JsonProcessingException {
        String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        String redirectUri = getGoogleRedirectUri(origin);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("client_id", googleConfig.getClientId());
        body.put("client_secret", googleConfig.getClient_secretId());
        body.put("code", decode);
        body.put("grant_type", googleConfig.getGrant_type());
        body.put("redirect_uri", redirectUri);
        return oAuth2Util.getAccessToken(googleConfig.getAccessTokenRequestUrl(), headers, body);
    }

    private String getGoogleRedirectUri(String origin) {
        switch (origin) {
            case "production":
                return googleConfig.getRedirect_uri_feProd();
            case "belocal":
                return googleConfig.getRedirect_uri_beLocal();
            case "beprod":
                return googleConfig.getRedirect_uri_beProd();
            default:
                return googleConfig.getRedirect_uri_feLocal();
        }
    }

    public ResponseEntity<ApiResponseWrapper> loginApple(String code, String origin) throws ParseException, IOException, JOSEException {
        String identityToken = getAppleIdentityToken(code, origin);
        if (!validateAppleIdToken(identityToken)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("애플로그인 정보 오류 발생."));
        }
        SignedJWT signedJWT = SignedJWT.parse(identityToken);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        String email = claims.getStringClaim("email");
        String name = claims.getStringClaim("name");
        String sub = claims.getSubject();
        String provider = appleConfig.getClient_name();
        AtomicBoolean isMember = new AtomicBoolean(true);
        Member member = memberRepository.findByMemberUuid(sub)
                .orElseGet(() -> {
                    isMember.set(false); // 신규회원
                    return memberRepository.save(new Member(email, name, provider, sub));
                });
        if(!Objects.equals(member.getMemberUuid(), sub)){ // 제공사 고유식별자 값이 존재하지 않았거나 수정된 경우, 최신 uuid 업데이트
            member.updateMemberUuid(sub);
        }
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberUuid(), member.getRole().toString(), provider);
        saveOrUpdateRefreshToken(member, allTokens.getRefreshToken());
        UserInfoProcessDto userInfoProcessDto = createUserInfoProcessDto(member, isMember);
        LoginResponseDto loginResponseDto = new LoginResponseDto(allTokens, userInfoProcessDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
    }

    private String getAppleIdentityToken(String code, String origin){
        String redirectUri = getAppleRedirectUri(origin);
        String clientSecret = createClientSecret();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("grant_type", appleConfig.getGrant_type());
        bodyMap.put("code", code);
        bodyMap.put("redirect_uri", redirectUri);
        bodyMap.put("client_id", appleConfig.getClientId());
        bodyMap.put("client_secret", clientSecret);
        return oAuth2Util.getIdentityToken(appleConfig.getIdentityTokenRequestUri(), headers, bodyMap);
    }

    private String getAppleRedirectUri(String origin) {
        switch (origin) {
            case "production":
                return appleConfig.getRedirect_uri_feProd();
            default:
                return appleConfig.getRedirect_uri_beProd();
        }
    }

    private String createClientSecret(){
        PrivateKey privateKey = getPrivateKey();
        long nowMillis  = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        final String jwt = Jwts.builder()
                .setHeaderParam("kid", appleConfig.getKeyId())
                .setIssuer(appleConfig.getTeamId())
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + 86400000))// 1일
                .setAudience("https://appleid.apple.com")
                .setSubject(appleConfig.getClientId())
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
        return jwt;
    }

    private PrivateKey getPrivateKey() {
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleConfig.getPrivate_key());

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
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

    // 자동로그인
    public ResponseEntity<ApiResponseWrapper> autoLogin(HttpServletRequest request){
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("refreshToken");

        if(jwtUtil.validateTokenServiceLayer(accessToken)){
            // 액세스토큰 유효할 때
            String uuid = jwtUtil.getUuid(accessToken);
            Member member = memberRepository.findByMemberUuid(uuid).orElseThrow(
                    () -> new EntityNotFoundException("MEMBER ID가 존재하지 않습니다."));
            LoginResponseDto loginResponseDto = updatingLoginResponse(member);
            return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
        }
        if (jwtUtil.validateTokenServiceLayer(refreshToken)) {
            // 액세스토큰 유효x, 리프레시토큰 유효할 때
            String uuid = jwtUtil.getUuid(refreshToken);
            Member member = memberRepository.findByMemberUuid(uuid).orElseThrow(
                    () -> new EntityNotFoundException("MEMBER ID가 존재하지 않습니다."));
            LoginResponseDto loginResponseDto = updatingLoginResponse(member);
            return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseWrapper.fail("로그인이 필요합니다"));
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
                .pushYN(member.isPushYn())
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
                .pushYN(member.isPushYn())
                .build();
        return ResponseEntity.ok(ApiResponseWrapper.success(new MemberInfoResponseDto(userInfoProcessDto)));
    }

    public ResponseEntity<ApiResponseWrapper> signUpAgree(SignUpAgreeRequest signUpAgreeRequest, CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        signUpAgreementRepository.save(new Agreement(memberId, signUpAgreeRequest.privacy_policy(), signUpAgreeRequest.terms_of_service()));
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }
}
