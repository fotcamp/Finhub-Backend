package fotcamp.finhub.main.auth;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.dto.response.login.LoginResponseDto;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.main.service.AuthProviderClient;
import fotcamp.finhub.main.service.AuthService2;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("classpath:application-test.yaml")
@Transactional
public class AutoLoginTest {
    @MockBean
    private AuthProviderClient authProviderClient;

    @Autowired
    private AuthService2 authService2;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        String uuid = "test-uuid";
        Member member = new Member("testUser@example.com", "testUser", "google", uuid);
        memberRepository.saveAndFlush(member);

        when(jwtUtil.createAllTokens(anyString(), anyString(), anyString()))
                .thenReturn(new TokenDto("mockAccessToken", "mockRefreshToken"));
    }

    @Test
    @DisplayName("자동 로그인 - 액세스 토큰 유효할 때")
    public void testAutoLogin_withValidAccessToken() {
        String accessToken = "validAccessToken";
        String uuid = "test-uuid";
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + accessToken);
        when(jwtUtil.validateTokenServiceLayer(accessToken)).thenReturn(true);
        when(jwtUtil.getUuid(accessToken)).thenReturn(uuid);

        LoginResponseDto result = authService2.autoLogin(request);

        assertNotNull(result);
        assertNotNull(result.getToken().getAccessToken());
        assertNotNull(result.getToken().getRefreshToken());
    }

    @Test
    @DisplayName("자동 로그인 - 리프레시 토큰 유효할 때")
    public void testAutoLogin_withValidRefreshToken() {
        String refreshToken = "validRefreshToken";
        String uuid = "test-uuid";
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("refreshToken")).thenReturn(refreshToken);
        when(jwtUtil.validateTokenServiceLayer(refreshToken)).thenReturn(true);
        when(jwtUtil.getUuid(refreshToken)).thenReturn(uuid);

        LoginResponseDto result = authService2.autoLogin(request);

        assertNotNull(result);
        assertNotNull(result.getToken().getAccessToken());
        assertNotNull(result.getToken().getRefreshToken());
    }

    @Test
    @DisplayName("자동 로그인 - 액세스 토큰 및 리프레시 토큰 모두 유효하지 않을 때")
    public void testAutoLogin_withInvalidTokens() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("notValidAccessToken")).thenReturn(null);
        when(request.getHeader("notValidRefreshToken")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService2.autoLogin(request);
        });

        assertEquals("로그인이 필요합니다", exception.getMessage());
    }
}
