package fotcamp.finhub.main.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.SignUpAgreeRequest;
import fotcamp.finhub.main.service.AuthService2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

@Tag(name = "A auth",
    description = "액세스 토큰 받아오는 방법 \n\n" +
            "kakao 인가코드를 먼저 받아온다. \n\n" +
            "BE local Test : https://kauth.kakao.com/oauth/authorize?client_id=8cdab51912aa75e69f1dce7eb88d196c&redirect_uri=http://localhost:8090/api/v1/auth/login/oauth2/callback/kakao&response_type=code \n\n" +
            "BE dev Test : https://kauth.kakao.com/oauth/authorize?client_id=8cdab51912aa75e69f1dce7eb88d196c&redirect_uri=http://15.164.149.101:3000/auth/kakao/callback&response_type=code \n\n" +
            "BE main Test : https://kauth.kakao.com/oauth/authorize?client_id=8cdab51912aa75e69f1dce7eb88d196c&redirect_uri=https://main.fin-hub.co.kr/auth/kakao/callback&response_type=code \n\n" +
            "\n\n"+
            "google 인가코드 받아오기 \n\n"+
            "BE local : https://accounts.google.com/o/oauth2/v2/auth?client_id=353339464651-dnul84p5jsljqkg1gfsgsdoqol5ci1ak.apps.googleusercontent.com&redirect_uri=http://localhost:8090/api/v1/auth/login/oauth2/callback/google&response_type=code&scope=profile email \n\n"+
            "BE main : https://accounts.google.com/o/oauth2/v2/auth?client_id=353339464651-dnul84p5jsljqkg1gfsgsdoqol5ci1ak.apps.googleusercontent.com&redirect_uri=https://main.fin-hub.co.kr/auth/google/callback&response_type=code&scope=profile email\n\n"+
            "\n\n"+
            "apple 인가코드 받아오기 \n\n"+
            "production : https://appleid.apple.com/auth/authorize?response_type=code&client_id=finhub.fotcamp.com&redirect_uri=https://api.fin-hub.co.kr/api/v1/auth/login/oauth2/callback/apple&response_mode=query\n\n\n"+
            "\n\n"+
            "BE local -> origin parameter에 belocal\n\n" +
            "BE dev -> origin parameter에 bedev\n\n" +
            "BE prod -> origin parameter에 beprod\n\n" +
"FE는 origin parameter에 local, dev, production으로 자동 세팅 해야함."
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService2 authService2;
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
        return authService2.loginKakao(code, origin);
    }

    @GetMapping("/login/oauth2/callback/google")
    @Operation(summary = " 구글 로그인", description = "구글 서버로부터 받은 액세스토큰 넣어서 진행하는 로그인절차")
    public ResponseEntity<ApiResponseWrapper> googleLogin( @RequestParam(name = "code")String code,
                                                           @RequestParam(name = "origin") String origin) throws JsonProcessingException {
        return authService2.loginGoogle(code, origin);
    }

    @GetMapping("/login/oauth2/callback/apple")
    @Operation(summary = " 애플 로그인", description = "애플 서버로부터 받은 액세스토큰 넣어서 진행하는 로그인절차")
    public ResponseEntity<ApiResponseWrapper> appleLogin(@RequestParam(name = "code") String code,
                                                         @RequestParam(name = "origin") String origin) throws IOException, ParseException, JOSEException {
        return authService2.loginApple(code, origin);
    }

    @GetMapping("/updateAccessToken") //헤더에 bearer 토큰 담지 말고 전송!
    @Operation(summary = "리프레시토큰 만료될 때 액세스토큰 업데이트 ", description = "액세스토큰 발급")
    public ResponseEntity<ApiResponseWrapper> validRefreshToken(
            HttpServletRequest request){
        return authService2.validRefreshToken(request);
    }

    // 자동로그인
    @GetMapping("/autoLogin")
    @Operation(summary = "자동로그인", description = "자동로그인")
    public ResponseEntity<ApiResponseWrapper> autoLogin(HttpServletRequest request){
        return authService2.autoLogin(request);
    }

    // 단순 정보 제공용
    @GetMapping("/info")
    @Operation(summary = "단순 멤버 정보제공", description = "단순 정보제공")
    public ResponseEntity<ApiResponseWrapper> memberInfoRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return authService2.memberInfoRequest(userDetails);
    }

    @PostMapping("/agreement")
    @Operation(summary = "회원가입 이용 약관 동의", description = "회원가입 이용 약관 동의 api")
    public ResponseEntity<ApiResponseWrapper> signUpAgree(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SignUpAgreeRequest signUpAgreeRequest){
        return authService2.signUpAgree(signUpAgreeRequest, userDetails);
    }

}
