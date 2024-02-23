package fotcamp.finhub.common.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.dto.request.SignupRequestDto;
import fotcamp.finhub.common.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /** 회원가입 */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseWrapper> signup(@RequestBody SignupRequestDto signupRequestDto){
        return memberService.signup(signupRequestDto);
    }

    @GetMapping("/test1")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseWrapper> test1(){return memberService.test1();}

    @GetMapping("/test2")
    public ResponseEntity<ApiResponseWrapper> test2(){
        return memberService.test2();
    }

}
