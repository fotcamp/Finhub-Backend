package fotcamp.finhub.main.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.main.dto.request.SignupRequestDto;
import fotcamp.finhub.main.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /** 회원가입 */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseWrapper> signup1(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.signup(signupRequestDto);
    }

    @GetMapping("/test1")
//    @PreAuthorize("hasRole('SUPADMIN')")
    // 로그인을 한 유저만 접근 가능한 api
    public ResponseEntity<ApiResponseWrapper> test1(){
        System.out.println("3--");
        return memberService.test1();
    }

    @GetMapping("/test2")
    // 로그인을 하든 안하든 접근이 가능한 api
    public ResponseEntity<ApiResponseWrapper> test2(){
        return memberService.test2();
    }

}
