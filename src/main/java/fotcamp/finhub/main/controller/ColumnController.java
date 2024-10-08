package fotcamp.finhub.main.controller;

import com.google.protobuf.Api;
import fotcamp.finhub.common.api.ApiCommonResponse;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.utils.PageableUtil;
import fotcamp.finhub.main.dto.request.BlockMemberRequestDto;
import fotcamp.finhub.main.dto.request.ScrapRequestDto;
import fotcamp.finhub.main.dto.response.column.CommentDeleteRequestDto;
import fotcamp.finhub.main.dto.response.column.CommentReportRequestDto;
import fotcamp.finhub.main.dto.response.column.CommentRequestDto;
import fotcamp.finhub.main.service.ColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "C main gpt column", description = "main gpt column api")
@RestController
@RequestMapping("/api/v1/main/column")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping
    @Operation(summary = "GPT 칼럼 목록 조회", description = "GPT 칼럼 목록 조회")
    public ResponseEntity<ApiResponseWrapper> getColumnList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "createdTime");
        return columnService.getColumnList(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "GPT 칼럼 상세 조회", description = "GPT 칼럼 상세 조회")
    public ResponseEntity<ApiResponseWrapper> getColumnDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "id") Long id
    ) {
        return columnService.getColumnDetail(userDetails, id);
    }

    @PostMapping("/like")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "GPT 칼럼 및 댓글 좋아요(1: GPT 칼럼, 2: 댓글)", description = "GPT 칼럼 및 댓글 좋아요")
    public ResponseEntity<ApiResponseWrapper> like(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ScrapRequestDto dto){
        return columnService.like(userDetails, dto);
    }

    @PostMapping("/comment")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "GPT 칼럼 댓글 작성", description = "GPT 칼럼 댓글 작성")
    public ResponseEntity<ApiResponseWrapper> comment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentRequestDto dto){
        return columnService.comment(userDetails, dto);
    }

    @GetMapping("/comment/{id}/{type}")
    @Operation(summary = "GPT 칼럼 댓글 조회", description = "GPT 칼럼 댓글 조회")
    public ResponseEntity<ApiResponseWrapper> getColumnComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "id", required = true) Long id,
            @PathVariable(name = "type", required = true) Long type,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "3") int size
    ) {
        Pageable pageable = PageableUtil.createPageableWithNoSort(page, size);
        return columnService.getColumnComment(userDetails, id, type, pageable);
    }

    @PutMapping("/comment/actions")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "GPT 칼럼 댓글 수정", description = "GPT 칼럼 댓글 수정")
    public ResponseEntity<ApiResponseWrapper> commentPut(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentRequestDto dto){
        return columnService.commentPut(userDetails, dto);
    }

    @DeleteMapping("/comment/actions")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "GPT 칼럼 댓글 삭제", description = "GPT 칼럼 댓글 삭제")
    public ResponseEntity<ApiResponseWrapper> commentDelete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentDeleteRequestDto dto){
        return columnService.commentDelete(userDetails, dto);
    }

    @GetMapping("/comment/actions")
    @Operation(summary = "GPT 칼럼 댓글 신고 이유 조회", description = "GPT 칼럼 댓글 신고 이유 조회")
    public ResponseEntity<ApiResponseWrapper> commentReasons(){
        return columnService.commentReasons();
    }

    @PostMapping("/comment/actions")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "GPT 칼럼 댓글 신고", description = "GPT 칼럼 댓글 신고")
    public ResponseEntity<ApiCommonResponse<Void>> commentReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentReportRequestDto dto
    ){
        return columnService.commentReport(userDetails, dto);
    }

    @PostMapping("/block")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "사용자 차단", description = "사용자 차단")
    public ResponseEntity<ApiResponseWrapper> blockMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BlockMemberRequestDto dto
            ){
        return columnService.blockMember(userDetails, dto);
    }
}
