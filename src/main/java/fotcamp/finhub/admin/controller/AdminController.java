package fotcamp.finhub.admin.controller;

import fotcamp.finhub.admin.dto.CreateCategoryDto;
import fotcamp.finhub.admin.dto.LoginDto;
import fotcamp.finhub.admin.dto.ModifyCategoryDto;
import fotcamp.finhub.admin.service.AdminService;
import fotcamp.finhub.common.domain.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Tag(name = "admin", description = "admin api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인 요청", description = "관리자 여부 판단", tags = {"AdminController"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto) {
        return adminService.login(loginDto);
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리 전체 조회", description = "category 전체 조회", tags = {"AdminController"})
    public ResponseEntity<List<Category>> getAllCategory() {
        return adminService.getAllCategory();
    }

    @PostMapping("/category")
    @Operation(summary = "카테고리 생성", description = "category 등록", tags = {"AdminController"})
    public ResponseEntity<Void> createCategory(@RequestBody CreateCategoryDto createCategoryDto) {
        return adminService.createCategory(createCategoryDto);
    }

    @PutMapping("/category")
    @Operation(summary = "카테고리 수정", description = "category 보이기/숨기기 수정", tags = {"AdminController"})
    public ResponseEntity<Void> modifyCategory(@RequestBody ModifyCategoryDto modifyCategoryDto) {
        return adminService.modifyCategory(modifyCategoryDto);
    }
}
