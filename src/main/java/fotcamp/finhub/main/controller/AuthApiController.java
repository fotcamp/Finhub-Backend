package fotcamp.finhub.main.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.main.dto.request.LoginRequestDto;
import fotcamp.finhub.main.service.AuthService;
import io.lettuce.core.ScriptOutputType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    /** 로그인 사용자 인증절차 */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto){
        System.out.println("login controllllllllllllll");
        return authService.login(loginRequestDto);
    }

    @PostMapping("/updateAccessToken") //헤더에 bearer 토큰 담지 말고 전송!
    public ResponseEntity<ApiResponseWrapper> validRefreshToken(
            HttpServletRequest request){
        return authService.validRefreshToken(request);
    }
}
