package fotcamp.finhub.common.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.dto.request.LoginRequestDto;
import fotcamp.finhub.common.service.AuthService;
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
        return authService.login(loginRequestDto);
    }

    @GetMapping("/updateAccessToken") //헤더에 bearer 토큰 담지 말고 전송!
    public ResponseEntity<ApiResponseWrapper> validRefreshToken(
            HttpServletRequest request){
        return authService.validRefreshToken(request);
    }
}
