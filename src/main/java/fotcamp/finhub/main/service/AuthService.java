package fotcamp.finhub.main.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.RefreshToken;
import fotcamp.finhub.common.security.CustomAccessDeniedHandler;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.main.config.KakaoConfig;
import fotcamp.finhub.main.dto.process.login.UserInfoProcessDto;
import fotcamp.finhub.main.dto.request.AutoLoginRequestDto;
import fotcamp.finhub.main.dto.response.login.LoginResponseDto;
import fotcamp.finhub.main.dto.process.login.KakaoUserInfoProcessDto;
import fotcamp.finhub.main.dto.response.login.MemberInfoResponseDto;
import fotcamp.finhub.main.dto.response.login.UpdateAccessTokenResponseDto;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

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
    private final AwsS3Service awsS3Service;

    @Value("${custom-redirect-uri.felocal}")
    private String redirect_uri_feLocal;
    @Value("${custom-redirect-uri.fedev}")
    private String redirect_uri_feDev;
    @Value("${custom-redirect-uri.feprod}")
    private String redirect_uri_feProd;
    @Value("${custom-redirect-uri.belocal}")
    private String redirect_uri_beLocal;
    @Value("${custom-redirect-uri.bedev}")
    private String redirect_uri_beDev;
    @Value("${custom-redirect-uri.beprod}")
    private String redirect_uri_beProd;

    public ResponseEntity<ApiResponseWrapper> login(String code, String origin) throws JsonProcessingException {
        log.info("***start login service !! " + origin);
        String kakaoAccessToken = getKakaoAccessToken(code, origin); // 1. 액세스토큰 요청
        log.info("***origin " + origin);
        log.info("***code " + code);
        KakaoUserInfoProcessDto kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken); // 2. 사용자 정보 반환
        String email = kakaoUserInfo.getEmail();
        String name = kakaoUserInfo.getName();
        // 3. 사용자 가입 유무 확인`
        Member member = memberRepository.findByEmail(email).orElseGet( () -> memberRepository.save(new Member(email, name)));

        // 4. jwt 발급
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberId(), member.getRole().toString());

        // 5. 리프레시 토큰 저장
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
        // 6. 응답 데이터 생성: 닉네임, 이메일, 유저아바타 이미지, 직업명, 직업아바타이미지, 푸시알림 정보
        UserInfoProcessDto userInfoProcessDto = UserInfoProcessDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .avatarUrl(member.getUserAvatar() != null ? awsS3Service.combineWithBaseUrl(member.getUserAvatar().getAvatar_img_path()) : null)
                .userType(member.getUserType() != null ? member.getUserType().getName() : null)
                .userTypeUrl(member.getUserType() != null ? awsS3Service.combineWithBaseUrl(member.getUserType().getAvatarImgPath()) : null)
                .pushYN(member.isPushYn())
                .build();
        LoginResponseDto loginResponseDto = new LoginResponseDto(allTokens, userInfoProcessDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
    }

    public String getKakaoAccessToken(String code, String origin) throws JsonProcessingException {
        log.info("***start get Kakao Access Token method!!");
        String redirectUri = redirect_uri_feLocal; // default 프론트 로컬

        if ("dev".equals(origin)) { // 프론트 개발
            redirectUri = redirect_uri_feDev;
        } else if ("production".equals(origin)) {  // 프론트 운영
            redirectUri = redirect_uri_feProd;
        } else if ("belocal".equals(origin)) { // 백엔드 로컬
            redirectUri = redirect_uri_beLocal;
        } else if ("bedev".equals(origin)) { // 백엔드 개발
            redirectUri = redirect_uri_beDev;
        } else if ("beprod".equals(origin)) { // 백엔드 운영
            redirectUri = redirect_uri_beProd;
        }

        log.info("***redirectUri "+ redirectUri);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", kakaoConfig.getGrant_type());
        body.add("client_id", kakaoConfig.getClientId());
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", kakaoConfig.getClient_secretId());

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                kakaoConfig.getAccessTokenRequestUrl(),
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    public KakaoUserInfoProcessDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String >> kakaoUserInfo = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                kakaoConfig.getUser_info_uri(),
                HttpMethod.POST,
                kakaoUserInfo,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        return new KakaoUserInfoProcessDto(nickname,email);
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
