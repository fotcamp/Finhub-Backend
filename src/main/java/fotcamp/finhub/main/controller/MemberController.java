package fotcamp.finhub.main.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.ChangeNicknameRequestDto;
import fotcamp.finhub.main.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "main", description = "main api")
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

    @PostMapping("/setting/nickname")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 닉네임 변경", description = "nickname change", tags = {"MemberController"})
    public ResponseEntity<ApiResponseWrapper> changeNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChangeNicknameRequestDto dto){
        return memberService.changeNickname(userDetails, dto);
    }

    @GetMapping("/setting/resign")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 회원탈퇴", description = "membership resign", tags = {"MemberController"})
    public ResponseEntity<ApiResponseWrapper> resignMembership(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return memberService.membershipResign(userDetails);
    }

}
