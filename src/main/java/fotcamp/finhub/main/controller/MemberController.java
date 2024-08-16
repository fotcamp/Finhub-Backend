package fotcamp.finhub.main.controller;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetailService;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.EmailUpdateDto;
import fotcamp.finhub.main.dto.request.ScrapRequestDto;
import fotcamp.finhub.main.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저정보 API", description = "유저관련 정보를 조작한다.")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PatchMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "유저 이메일정보 설정", description = "유저의 이메일정보를 설정 및 변경한다.")
    public ResponseEntity<ApiResponseWrapper> modifyEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody EmailUpdateDto emailDto){
        memberService.modifyEmail(emailDto.email(), userDetails.getMemberIdasLong());
        return null;
    }
}
