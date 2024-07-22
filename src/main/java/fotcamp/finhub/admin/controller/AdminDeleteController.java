package fotcamp.finhub.admin.controller;

import com.google.protobuf.Api;
import fotcamp.finhub.admin.dto.request.*;
import fotcamp.finhub.admin.service.AdminDeleteService;
import fotcamp.finhub.admin.service.AdminService;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "F admin delete", description = "admin delte api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminDeleteController {

    private final AdminDeleteService adminDeleteService;

    @DeleteMapping("/category")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "카테고리 삭제", description = "카테고리에 속한 모든 토픽 외래키필드 NULL")
    public ResponseEntity<ApiResponseWrapper> deleteCategory(
            @Valid @RequestBody DeleteCategoryRequestDto dto)
    {
        return adminDeleteService.deleteCategory(dto);
    }

    @DeleteMapping("/topic")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "토픽 삭제", description = "토픽삭제")
    public ResponseEntity<ApiResponseWrapper> deleteTopic(
            @Valid @RequestBody DeleteTopicRequestDto dto)
    {
        return adminDeleteService.deleteTopic(dto);
    }

    @DeleteMapping("/usertype")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "유저타입 삭제", description = "유저타입삭제")
    public ResponseEntity<ApiResponseWrapper> deleteUsertype(
            @Valid @RequestBody DeleteUsertypeRequestDto dto
            ){
        return adminDeleteService.deleteUsertype(dto);
    }

    @DeleteMapping("/avatar")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "아바타 삭제 ", description = "아바타 삭제")
    public ResponseEntity<ApiResponseWrapper> deleteAvatar(
            @Valid @RequestBody DeleteAvatarRequestDto dto
    ){
        return adminDeleteService.deleteAvatar(dto);
    }

    @DeleteMapping("/quiz")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "퀴즈 삭제 ", description = "퀴즈 삭제")
    public ResponseEntity<ApiResponseWrapper> deleteQuiz(
            @Valid @RequestBody DeleteQuizRequestDto dto
            ){
        return adminDeleteService.deleteQuiz(dto);
    }

    @DeleteMapping("/column")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "컬럼 삭제 ", description = "컬럼 삭제")
    public ResponseEntity<ApiResponseWrapper> deleteColumn(
            @Valid @RequestBody DeleteColumnRequestDto dto
    ){
        return adminDeleteService.deleteColumn(dto);
    }

    @DeleteMapping("/calendar-emoticon")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "캘린더 이모티콘 삭제 ", description = "캘린더 이모티콘 삭제")
    public ResponseEntity<ApiResponseWrapper> deleteEmoji(
            @Valid @RequestBody DeleteCalendarEmojiRequestDto dto
    ){
        return adminDeleteService.deleteEmoji(dto);
    }

    @DeleteMapping("/report/reason")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "신고사유 삭제", description = "신고사유 삭제")
    public ResponseEntity<ApiResponseWrapper> deleteReportReason(
            @Valid @RequestBody DeleteReportReasonRequestDto dto
    ){
        return adminDeleteService.deleteReportReason(dto);
    }

    @DeleteMapping("/banner")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "배너 삭제", description = "배너 삭제")
    public ResponseEntity<ApiResponseWrapper> deleteBanner(
        @RequestBody DeleteBannerRequestDto dto
    ){
        return adminDeleteService.deleteBanner(dto);
    }
}
