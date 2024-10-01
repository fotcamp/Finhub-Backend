package fotcamp.finhub.main.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.config.KakaoConfig;
import fotcamp.finhub.main.dto.process.login.KakaoUserInfoProcessDto;
import fotcamp.finhub.main.dto.response.login.LoginResponseDto;
import fotcamp.finhub.main.repository.AgreementRepository;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.main.service.AuthService2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AgreementRepository agreementRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private KakaoConfig kakaoConfig;

    @InjectMocks
    private AuthService2 authService2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("이미 회원가입된 유저의 카카오로그인기능을 테스트한다.")
    public void testLoginKakao_withExistingMember() throws JsonProcessingException {
        // Given
        String code = "fake_code";
        String origin = "fake_origin";
        String kakaoAccessToken = "fake_access_token";
        KakaoUserInfoProcessDto kakaoUserInfo = new KakaoUserInfoProcessDto("fake_email", "fake_name", "fake_uuid");
        String provider = "kakao";

        // Mocking external dependencies
        when(authService2.getKakaoAccessToken(anyString(), "belocal")).thenReturn(kakaoAccessToken);
        when(authService2.getKakaoUserInfo(anyString())).thenReturn(kakaoUserInfo);
        when(kakaoConfig.getClient_name()).thenReturn(provider);

        Member existingMember = new Member("fake_email", "fake_name", provider, "fake_uuid");
        when(memberRepository.findByMemberUuid(anyString())).thenReturn(Optional.of(existingMember));
        when(agreementRepository.existsByMemberAndPrivacyPolicyTrueAndTermsOfServiceTrue(existingMember)).thenReturn(true);

        TokenDto tokenDto = new TokenDto("fake_access_token", "fake_refresh_token");
        when(jwtUtil.createAllTokens(anyString(), anyString(), anyString())).thenReturn(tokenDto);

        // When
        ResponseEntity<ApiResponseWrapper> response = authService2.loginKakao(code, origin);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, ((LoginResponseDto) Objects.requireNonNull(response.getBody()).data()).getInfo().getIsMember());
        verify(memberRepository, never()).save(any(Member.class));  // no new member should be created
    }

    @Test
    public void testLoginKakao_withNewMember() throws JsonProcessingException {
        // Given
        String code = "fake_code";
        String origin = "fake_origin";
        String kakaoAccessToken = "fake_access_token";
        KakaoUserInfoProcessDto kakaoUserInfo = new KakaoUserInfoProcessDto("new_email", "new_name", "new_uuid");
        String provider = "kakao";

        // Mocking external dependencies
        when(authService2.getKakaoAccessToken(anyString(), anyString())).thenReturn(kakaoAccessToken);
        when(authService2.getKakaoUserInfo(anyString())).thenReturn(kakaoUserInfo);
        when(kakaoConfig.getClient_name()).thenReturn(provider);

        // New member scenario
        Member newMember = new Member("new_email", "new_name", provider, "new_uuid");
        when(memberRepository.findByMemberUuid(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(newMember);

        when(agreementRepository.existsByMemberAndPrivacyPolicyTrueAndTermsOfServiceTrue(newMember)).thenReturn(false);

        TokenDto tokenDto = new TokenDto("fake_access_token", "fake_refresh_token");
        when(jwtUtil.createAllTokens(anyString(), anyString(), anyString())).thenReturn(tokenDto);

        // When
        ResponseEntity<ApiResponseWrapper> response = authService2.loginKakao(code, origin);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(false, ((LoginResponseDto) Objects.requireNonNull(response.getBody()).data()).getInfo().getIsMember()); // new member case
        verify(memberRepository, times(1)).save(any(Member.class));  // new member should be created
    }
}
