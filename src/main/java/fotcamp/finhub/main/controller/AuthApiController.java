package fotcamp.finhub.main.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.main.dto.request.AutoLoginRequestDto;
import fotcamp.finhub.main.service.AuthService;
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

    // https://kauth.kakao.com/oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code

    /** REDIRECT URI -> 프론트에서 인가코드를 포함시켜서 전송 */
    @GetMapping("/login/oauth2/callback/kakao")
    public ResponseEntity<ApiResponseWrapper> kakaoLogin(@RequestParam(name = "code") String code) throws JsonProcessingException {
        // 카카오서버로 인가코드와 관련 정보를 전송해서 카카오가 발행하는 액세스토큰이 정상적으로 수신되면,
        // 자체 jwt 발행하여 서비스 권한부여
        return authService.login(code);
    }

    @PostMapping("/updateAccessToken") //헤더에 bearer 토큰 담지 말고 전송!
    public ResponseEntity<ApiResponseWrapper> validRefreshToken(
            HttpServletRequest request){
        return authService.validRefreshToken(request);
    }

    // 자동로그인
    @PostMapping("/autoLogin")
    public ResponseEntity<ApiResponseWrapper> autoLogin(@RequestBody @Valid AutoLoginRequestDto autoLoginRequestDto){
        return authService.autoLogin(autoLoginRequestDto);
    }
}
