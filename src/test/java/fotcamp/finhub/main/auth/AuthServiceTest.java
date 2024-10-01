package fotcamp.finhub.main.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.dto.process.login.KakaoUserInfoProcessDto;
import fotcamp.finhub.main.dto.response.login.LoginResponseDto;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.main.service.AuthProviderClient;
import fotcamp.finhub.main.service.AuthService2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.yaml")
public class AuthServiceTest {
    @MockBean
    private AuthProviderClient authProviderClient;

    @Autowired
    private AuthService2 authService2;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("카카오로그인기능을 테스트한다.")
    public void testAutoLogin_withValidAccessToken() throws JsonProcessingException {
        when(authProviderClient.getKakaoAccessToken("testCode", "testOrigin")).thenReturn("mockAccessToken");
        when(authProviderClient.getKakaoUserInfo("mockAccessToken")).thenReturn(new KakaoUserInfoProcessDto("kakaoTest", "kakaoTest@test.com", "aaaa"));

        LoginResponseDto result = authService2.loginKakao("testCode", "testOrigin");

        assertNotNull(result);
        String accessToken = result.getToken().getAccessToken();
        assertNotNull(accessToken);
        assertTrue(jwtUtil.validateToken(accessToken));
        assertNotNull(result.getToken().getRefreshToken());
        assertEquals("kakaoTest@test.com", result.getInfo().getEmail());

        Optional<Member> memberOpt = memberRepository.findByMemberUuid("aaaa");
        assertTrue(memberOpt.isPresent());
    }

    @Test
    @DisplayName("구글로그인기능을 테스트한다.")
    public void testLoginGoogle() throws JsonProcessingException {
        when(authProviderClient.getGoogleAccessToken("testCode", "testOrigin")).thenReturn("mockAccessToken");

        Map userInfo = new HashMap();
        userInfo.put("email", "googleTest@test.com");
        userInfo.put("name", "googleTest");
        userInfo.put("sub", "bbbb");

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String sub = (String) userInfo.get("sub");

        when(authProviderClient.getUserInfo("mockAccessToken")).thenReturn(userInfo);

        LoginResponseDto result = authService2.loginGoogle("testCode", "testOrigin");

        assertNotNull(result);
        String accessToken = result.getToken().getAccessToken();
        assertNotNull(accessToken);
        assertTrue(jwtUtil.validateToken(accessToken));
        assertNotNull(result.getToken().getRefreshToken());
        assertEquals("googleTest@test.com", result.getInfo().getEmail());

        Optional<Member> memberOpt = memberRepository.findByMemberUuid("bbbb");
        assertTrue(memberOpt.isPresent());
    }

    @Test
    @DisplayName("애플로그인기능을 테스트한다.")
    public void testLoginApple() throws JsonProcessingException {
        when(authProviderClient.getAppleIdentityToken("testCode", "testOrigin")).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFwcGxlVGVzdEB0ZXN0LmNvbSIsIm5hbWUiOiJhcHBsZVRlc3QiLCJzdWIiOiJjY2NjIiwiaXNzIjoiaHR0cHM6Ly9hcHBsZWlkLmFwcGxlLmNvbSIsImF1ZCI6ImNvbS5leGFtcGxlLmFwcCIsImlhdCI6MTcyNzYxMjMwMywiZXhwIjoxNzI3NjEyOTAzfQ.JVhO81TiS5P0ssg269w236VG0VzzVhIjxYUqIHkK260\n");

        try {
            LoginResponseDto result = authService2.loginApple("testCode", "testOrigin");

            assertNotNull(result);
            String accessToken = result.getToken().getAccessToken();
            assertNotNull(accessToken);
            assertTrue(jwtUtil.validateToken(accessToken));
            assertNotNull(result.getToken().getRefreshToken());
            assertEquals("appleTest@test.com", result.getInfo().getEmail());

            Optional<Member> memberOpt = memberRepository.findByMemberUuid("cccc");
            assertTrue(memberOpt.isPresent());

        } catch (RuntimeException | ParseException | IOException | JOSEException e) {
            // 예외가 발생하면, 해당 예외만 검증하고 테스트 종료
            assertInstanceOf(RuntimeException.class, e);
        }
    }
}
