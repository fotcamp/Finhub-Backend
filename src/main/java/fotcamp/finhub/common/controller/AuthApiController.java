package fotcamp.finhub.common.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.dto.LoginRequestDto;
import fotcamp.finhub.common.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
