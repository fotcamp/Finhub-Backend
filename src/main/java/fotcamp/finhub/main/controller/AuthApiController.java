package fotcamp.finhub.main.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.AutoLoginRequestDto;
import fotcamp.finhub.main.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "A auth",
    description = "액세스 토큰 받아오는 방법 \n\n" +
            "kakao 인가코드를 먼저 받아온다. \n\n" +
            "BE local, dev Test : https://kauth.kakao.com/oauth/authorize?client_id=8cdab51912aa75e69f1dce7eb88d196c&redirect_uri=http://localhost:8090/api/v1/auth/login/oauth2/callback/kakao&response_type=code \n\n" +
            "BE main Test : https://kauth.kakao.com/oauth/authorize?client_id=8cdab51912aa75e69f1dce7eb88d196c&redirect_uri=https://api.fin-hub.co.kr/api/v1/auth/login/oauth2/callback/kakao&response_type=code \n\n" +
            "BE local -> origin parameter에 belocal\n\n" +
            "BE dev -> origin parameter에 bedev\n\n" +
            "BE prod -> origin parameter에 beprod\n\n" +
            "FE는 origin parameter에 local, dev, production으로 자동 세팅 해야함."
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;
    // https://kauth.kakao.com/oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code

    /**
     * REDIRECT URI -> 프론트에서 인가코드를 포함시켜서 전송
     */
    @GetMapping("/login/oauth2/callback/kakao")
    @Operation(summary = " 카카오 로그인", description = "카카오서버로부터 받은 액세스토큰 넣어서 진행하는 로그인절차")
    public ResponseEntity<ApiResponseWrapper> kakaoLogin(@RequestParam(name = "code") String code,
                                                         @RequestParam(name = "origin") String origin) throws JsonProcessingException {

        // 카카오서버로 인가코드와 관련 정보를 전송해서 카카오가 발행하는 액세스토큰이 정상적으로 수신되면,
        // 자체 jwt 발행하여 서비스 권한부여
        return authService.login(code, origin);
    }

    @GetMapping("/updateAccessToken") //헤더에 bearer 토큰 담지 말고 전송!
    @Operation(summary = "리프레시토큰 만료될 때 액세스토큰 업데이트 ", description = "액세스토큰 발급")
    public ResponseEntity<ApiResponseWrapper> validRefreshToken(
            HttpServletRequest request){
        return authService.validRefreshToken(request);
    }

    // 자동로그인
    @GetMapping("/autoLogin")
    @Operation(summary = "자동로그인", description = "자동로그인")
    public ResponseEntity<ApiResponseWrapper> autoLogin(HttpServletRequest request){
        return authService.autoLogin(request);
    }

    // 단순 정보 제공용
    @GetMapping("/info")
    @Operation(summary = "단순 멤버 정보제공", description = "단순 정보제공")
    public ResponseEntity<ApiResponseWrapper> memberInfoRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return authService.memberInfoRequest(userDetails);
    }

}
