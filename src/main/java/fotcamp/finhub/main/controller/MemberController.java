package fotcamp.finhub.main.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.main.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test1")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> test1(){
        return ResponseEntity.ok(ApiResponseWrapper.success("ok1"));
    }

    @GetMapping("/test2")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    public ResponseEntity<ApiResponseWrapper> test2(){
        return ResponseEntity.ok(ApiResponseWrapper.success("ok2"));
    }
}
