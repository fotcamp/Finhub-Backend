package fotcamp.finhub.admin.controller;

import fotcamp.finhub.admin.dto.*;
import fotcamp.finhub.admin.service.AdminService;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponseWrapper> login(@RequestBody LoginDto loginDto) {
        return adminService.login(loginDto);
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리 전체 조회", description = "category 전체 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getAllCategory() {
        return adminService.getAllCategory();
    }

    @PostMapping("/category")
    @Operation(summary = "카테고리 생성", description = "category 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createCategory(@RequestBody CreateCategoryDto createCategoryDto) {
        return adminService.createCategory(createCategoryDto);
    }

    @PutMapping("/category")
    @Operation(summary = "카테고리 수정", description = "category 보이기/숨기기 수정", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyCategory(@RequestBody ModifyCategoryDto modifyCategoryDto) {
        return adminService.modifyCategory(modifyCategoryDto);
    }

    @PostMapping("/topic")
    @Operation(summary = "토픽 생성", description = "topic 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createTopic(@RequestBody CreateTopicDto createTopicDto) {
        return adminService.createTopic(createTopicDto);
    }

    @PostMapping("/usertype")
    @Operation(summary = "유저 타입 생성", description = "usertype 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createUserType(@RequestBody CreateUserTypeDto createUserTypeDto) {
        return adminService.createUserType(createUserTypeDto);
    }

    @PutMapping("/usertype")
    @Operation(summary = "유저 타입 수정", description = "usertype 수정", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyUserType(@RequestBody ModifyUserTypeDto modifyUserTypeDto) {
        return adminService.modifyUserType(modifyUserTypeDto);
    }

    @GetMapping("/usertype")
    @Operation(summary = "유저 타입 전체조회", description = "usertype 전체 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getAllUserType() {
        return adminService.getAllUserType();
    }
}
