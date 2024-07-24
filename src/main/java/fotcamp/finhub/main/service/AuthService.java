package fotcamp.finhub.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.RefreshToken;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.config.GoogleConfig;
import fotcamp.finhub.main.config.KakaoConfig;
import fotcamp.finhub.main.config.OAuth2Util;
import fotcamp.finhub.main.dto.process.login.KakaoUserInfoProcessDto;
import fotcamp.finhub.main.dto.process.login.UserInfoProcessDto;
import fotcamp.finhub.main.dto.response.login.LoginResponseDto;
import fotcamp.finhub.main.dto.response.login.MemberInfoResponseDto;
import fotcamp.finhub.main.dto.response.login.UpdateAccessTokenResponseDto;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.main.repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final KakaoConfig kakaoConfig;
    private final GoogleConfig googleConfig;
    private final AwsS3Service awsS3Service;
    private final OAuth2Util oAuth2Util;

    public ResponseEntity<ApiResponseWrapper> loginKakao(String code, String origin) throws JsonProcessingException {
        String kakaoAccessToken = getKakaoAccessToken(code, origin);
        KakaoUserInfoProcessDto kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);
        String email = kakaoUserInfo.getEmail();
        String name = kakaoUserInfo.getName();
        String provider = kakaoConfig.getClient_name();
        Member member = memberRepository.findByEmailAndProvider(email, provider).orElseGet(() -> memberRepository.save(new Member(email, name, provider)));
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberId(), member.getRole().toString());
        saveOrUpdateRefreshToken(member, allTokens.getRefreshToken());
        // 응답 데이터 생성: 닉네임, 이메일, 유저아바타 이미지, 직업명, 직업아바타이미지, 푸시알림 정보
        UserInfoProcessDto userInfoProcessDto = createUserInfoProcessDto(member);
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

        return oAuth2Util.getAccessToken(redirectUri, headers, body);
    }

    private String getKakaoRedirectUri(String origin) {
        switch (origin) {
            case "dev":
                return kakaoConfig.getRedirect_uri_feDev();
            case "production":
                return kakaoConfig.getRedirect_uri_feProd();
            case "belocal":
                return kakaoConfig.getRedirect_uri_beLocal();
            case "bedev":
                return kakaoConfig.getRedirect_uri_beDev();
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
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new KakaoUserInfoProcessDto(nickname, email);
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

    private UserInfoProcessDto createUserInfoProcessDto(Member member) {
        return UserInfoProcessDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .avatarUrl(member.getUserAvatar() != null ? awsS3Service.combineWithBaseUrl(member.getUserAvatar().getAvatar_img_path()) : null)
                .userType(member.getUserType() != null ? member.getUserType().getName() : null)
                .userTypeUrl(member.getUserType() != null ? awsS3Service.combineWithBaseUrl(member.getUserType().getAvatarImgPath()) : null)
                .pushYN(member.isPushYn())
                .build();
    }

    public ResponseEntity<ApiResponseWrapper> loginGoogle(String code, String origin) throws JsonProcessingException {
        String googleAccessToken = getGoogleAccessToken(code, origin);
        Map<String, Object> userInfo = oAuth2Util.getUserInfo("https://www.googleapis.com/oauth2/v3/userinfo", googleAccessToken);
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String provider = googleConfig.getClient_name();
        Member member = memberRepository.findByEmailAndProvider(email, provider).orElseGet(() -> memberRepository.save(new Member(email, name, provider)));
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberId(), member.getRole().toString());
        saveOrUpdateRefreshToken(member, allTokens.getRefreshToken());
        UserInfoProcessDto userInfoProcessDto = createUserInfoProcessDto(member);
        LoginResponseDto loginResponseDto = new LoginResponseDto(allTokens, userInfoProcessDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
    }

    private String getGoogleAccessToken(String code, String origin) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String redirectUri = getKakaoRedirectUri(origin);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("client_id", googleConfig.getClientId());
        body.put("client_secret", googleConfig.getClient_secretId());
        body.put("code", code);
        body.put("grant_type", "authorization_code");
        body.put("redirect_uri", redirectUri);
        return oAuth2Util.getAccessToken("https://oauth2.googleapis.com/token", headers, body);
    }

    private String getGoogleRedirectUri(String origin) {
        switch (origin) {
            case "dev":
                return googleConfig.getRedirect_uri_feDev();
            case "production":
                return googleConfig.getRedirect_uri_feProd();
            case "belocal":
                return googleConfig.getRedirect_uri_beLocal();
            case "bedev":
                return googleConfig.getRedirect_uri_beDev();
            case "beprod":
                return googleConfig.getRedirect_uri_beProd();
            default:
                return googleConfig.getRedirect_uri_feLocal();
        }
    }


    public ResponseEntity<ApiResponseWrapper> validRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("refreshToken");
        if(refreshToken!= null && jwtUtil.validateToken(refreshToken)){
            Long memberId = jwtUtil.getUserId(refreshToken);
            String  roleType = jwtUtil.getRoleType(refreshToken);
            String newAccessToken = jwtUtil.createToken(memberId, roleType,"Access");
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
            Long memberId = jwtUtil.getUserId(accessToken);
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new EntityNotFoundException("MEMBER ID가 존재하지 않습니다."));
            LoginResponseDto loginResponseDto = updatingLoginResponse(member);
            return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
        }
        if (jwtUtil.validateTokenServiceLayer(refreshToken)) {
            // 액세스토큰 유효x, 리프레시토큰 유효할 때
            Long memberId = jwtUtil.getUserId(refreshToken);
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new EntityNotFoundException("MEMBER ID가 존재하지 않습니다."));
            LoginResponseDto loginResponseDto = updatingLoginResponse(member);
            return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseWrapper.fail("로그인이 필요합니다"));
    }

    public LoginResponseDto updatingLoginResponse(Member member){
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberId(), member.getRole().toString());
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
                .avatarUrl(member.getUserAvatar() != null ? awsS3Service.combineWithBaseUrl(member.getUserAvatar().getAvatar_img_path()) : null)
                .userType(member.getUserType() != null ? member.getUserType().getName() : null)
                .userTypeUrl(member.getUserType() != null ? awsS3Service.combineWithBaseUrl(member.getUserType().getAvatarImgPath()) : null)
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
                .avatarUrl(member.getUserAvatar() != null ? awsS3Service.combineWithBaseUrl(member.getUserAvatar().getAvatar_img_path()) : null)
                .userType(member.getUserType() != null ? member.getUserType().getName() : null)
                .userTypeUrl(member.getUserType() != null ? awsS3Service.combineWithBaseUrl(member.getUserType().getAvatarImgPath()) : null)
                .pushYN(member.isPushYn())
                .build();
        return ResponseEntity.ok(ApiResponseWrapper.success(new MemberInfoResponseDto(userInfoProcessDto)));
    }
}
