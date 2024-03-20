package fotcamp.finhub.main.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.RefreshToken;
import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.main.config.KakaoConfig;
import fotcamp.finhub.main.dto.response.LoginResponseDto;
import fotcamp.finhub.main.dto.process.KakaoUserInfoDto;
import fotcamp.finhub.main.dto.request.AutoLoginRequestDto;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    public ResponseEntity<ApiResponseWrapper> login(String code) throws JsonProcessingException {
        String kakaoAccessToken = getKakaoAccessToken(code); // 1. 액세스토큰 요청
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken); // 2. 사용자 정보 반환
        String email = kakaoUserInfo.getEmail();
        String name = kakaoUserInfo.getName();
        // 3. 사용자 가입 유무 확인
        Member member = memberRepository.findByEmail(email).orElseGet(
                () -> memberRepository.save(new Member(email, name))
        );

        // 4. jwt 발급
        TokenDto allTokens = jwtUtil.createAllTokens(member.getMemberId(), member.getRole().toString());

        Optional<RefreshToken> existingRefreshToken = tokenRepository.findByEmail(member.getEmail());
        if (existingRefreshToken.isPresent()) {
            // 기존 리프레시 토큰 정보가 있는 경우, 새 리프레시 토큰으로 업데이트
            RefreshToken refreshToken = existingRefreshToken.get();
            refreshToken.updateToken(allTokens.getRefreshToken());
            tokenRepository.save(refreshToken);
        } else {
            // 기존 리프레시 토큰 정보가 없는 경우, 새로운 리프레시 토큰 저장
            tokenRepository.save(new RefreshToken(member.getEmail(), allTokens.getRefreshToken()));
        }
        LoginResponseDto loginResponseDto = new LoginResponseDto(allTokens.getAccessToken(),allTokens.getRefreshToken() ,member.getName(), member.getEmail());
        return ResponseEntity.ok(ApiResponseWrapper.success(loginResponseDto));
    }

    public String getKakaoAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", kakaoConfig.getGrant_type());
        body.add("client_id", kakaoConfig.getClientId());
        body.add("redirect_uri", kakaoConfig.getRedirect_uri());
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

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
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
        return new KakaoUserInfoDto(nickname,email);
    }


    public ResponseEntity<ApiResponseWrapper> validRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("refreshToken");
        if(refreshToken!= null && jwtUtil.validateToken(refreshToken)){
            Long memberId = jwtUtil.getUserId(refreshToken);
            String  roleType = jwtUtil.getRoleType(refreshToken);
            String newAccessToken = jwtUtil.createToken(memberId, roleType,"Access");
            return ResponseEntity.ok(ApiResponseWrapper.success(newAccessToken));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("헤더에 토큰이 없습니다."));
        }
    }

    // 자동로그인
    public ResponseEntity<ApiResponseWrapper> autoLogin(AutoLoginRequestDto autoLoginRequestDto){
        String accessToken = autoLoginRequestDto.getAccessToken();
        String refreshToken = autoLoginRequestDto.getRefreshToken();

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
        Optional<RefreshToken> existingRefreshToken = tokenRepository.findByEmail(member.getEmail());
        if (existingRefreshToken.isPresent()) {
            // 기존 리프레시 토큰 정보가 있는 경우, 새 리프레시 토큰으로 업데이트
            RefreshToken refreshToken = existingRefreshToken.get();
            refreshToken.updateToken(allTokens.getRefreshToken());
            tokenRepository.save(refreshToken);
        } else {
            // 기존 리프레시 토큰 정보가 없는 경우, 새로운 리프레시 토큰 저장
            tokenRepository.save(new RefreshToken(member.getEmail(), allTokens.getRefreshToken()));
        }
        return new LoginResponseDto(allTokens.getAccessToken(), allTokens.getRefreshToken(), member.getName(), member.getEmail());
    }

}
// https://iamipro.tistory.com/143