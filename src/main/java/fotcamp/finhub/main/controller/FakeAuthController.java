package fotcamp.finhub.main.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mockup 소셜로그인 인가코드 발급서버", description = "소셜로그인 기능 테스트를 위해 임의의 인가코드를 발급하는 Mockup API")
@RestController("/api/v1/fake/auth")
@Profile("!prod")
public class FakeAuthController {
    @GetMapping("/token/kakao")
    public String getKakaoAccessToken(){
        return "hello";
    }

    @GetMapping("/token/google")
    public String getGoogleAccessToken(){
        return "hello";
    }

    @GetMapping("/token/apple")
    public String getAppleIdentifyToken(){
        return "hello";
    }
}
