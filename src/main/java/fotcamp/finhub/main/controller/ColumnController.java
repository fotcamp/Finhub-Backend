package fotcamp.finhub.main.controller;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.utils.PageableUtil;
import fotcamp.finhub.main.dto.request.ScrapRequestDto;
import fotcamp.finhub.main.service.ColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "main gpt column", description = "main gpt column api")
@RestController
@RequestMapping("/api/v1/main/column")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping
    @Operation(summary = "컬럼 목록 조회", description = "컬럼 목록 조회")
    public ResponseEntity<ApiResponseWrapper> getColumnList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "createdTime");
        return columnService.getColumnList(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "컬럼 상세 조회", description = "컬럼 상세 조회")
    public ResponseEntity<ApiResponseWrapper> getColumnDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "id") Long id
    ) {
        return columnService.getColumnDetail(userDetails, id);
    }

    @PostMapping("/like")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "컬럼 및 댓글 좋아요(1: 컬럼, 2: 댓글)", description = "컬럼 및 댓글 좋아요")
    public ResponseEntity<ApiResponseWrapper> like(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ScrapRequestDto dto){
        return columnService.like(userDetails, dto);
    }
}
