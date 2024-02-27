package fotcamp.finhub.jwtTest;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.main.dto.request.LoginRequestDto;
import fotcamp.finhub.main.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;


@SpringBootTest
public class JwtTokenValidTest {


    @Autowired
    AuthService authService;

//    @BeforeEach(){
//
//    }

    @Test
    public void loginTest(){
        LoginRequestDto dto = new LoginRequestDto("a@naver.com", "pwd1");
        ResponseEntity<ApiResponseWrapper> responseDto = authService.login(dto);
        System.out.println(responseDto);
    }
}
