package fotcamp.finhub.admin.controller;

import fotcamp.finhub.admin.dto.request.*;
import fotcamp.finhub.admin.service.AdminService;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.utils.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@Tag(name = "admin", description = "admin api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인 요청", description = "관리자 여부 판단", tags = {"AdminController"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public ResponseEntity<ApiResponseWrapper> login(@RequestBody LoginRequestDto loginRequestDto) {
        return adminService.login(loginRequestDto);
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리 전체 조회", description = "category 전체 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getAllCategory(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYN", required = false) String useYN) {

        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllCategory(pageable, useYN);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "카테고리 상세 조회", description = "category 상세 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getDetailCategory(@PathVariable("categoryId") Long categoryId) {
        return adminService.getDetailCategory(categoryId);
    }

    @PostMapping(value = "/category")
    @Operation(summary = "카테고리 생성", description = "category 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createCategory(@Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        return adminService.createCategory(createCategoryRequestDto);
    }

    @PutMapping("/category")
    @Operation(summary = "카테고리 수정", description = "category 수정", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyCategory(@Valid @RequestBody ModifyCategoryRequestDto modifyCategoryRequestDto) {
        return adminService.modifyCategory(modifyCategoryRequestDto);
    }

    @GetMapping("/topic")
    @Operation(summary = "토픽 전체 조회", description = "topic 전체 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getAllTopic(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "categoryId", required = false) Long id,
            @RequestParam(name = "useYN", required = false) String useYN
    ) {
        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllTopic(pageable, id, useYN);
    }

    @GetMapping("/topic/{topicId}")
    @Operation(summary = "토픽 상세조회", description = "topic 상세 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getDetailTopic(@PathVariable(name = "topicId") Long topicId) {
        return adminService.getDetailTopic(topicId);
    }

    @PostMapping("/topic")
    @Operation(summary = "토픽 생성", description = "topic 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createTopic(@Valid @RequestBody CreateTopicRequestDto createTopicRequestDto) {
        return adminService.createTopic(createTopicRequestDto);
    }

    @PutMapping("/topic")
    @Operation(summary = "토픽 수정", description = "topic 수정", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyTopic(@Valid @RequestBody ModifyTopicRequestDto modifyTopicRequestDto) {
        return adminService.modifyTopic(modifyTopicRequestDto);
    }

    @GetMapping("/usertype")
    @Operation(summary = "유저 타입 전체조회", description = "usertype 전체 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getAllUserType(@RequestParam(name = "useYN", required = false) String useYN) {
        return adminService.getAllUserType(useYN);
    }

    @GetMapping("/usertype/{typeId}")
    @Operation(summary = "유저 타입 상세조회", description = "usertype 상세 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getDetailUserType(@PathVariable(name = "typeId") Long typeId) {
        return adminService.getDetailUserType(typeId);
    }

    @PostMapping("/usertype")
    @Operation(summary = "유저 타입 생성", description = "usertype 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createUserType(@Valid @RequestBody CreateUserTypeRequestDto createUserTypeRequestDto) {
        return adminService.createUserType(createUserTypeRequestDto);
    }

    @PutMapping("/usertype")
    @Operation(summary = "유저 타입 수정", description = "usertype 수정", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyUserType(@Valid @RequestBody ModifyUserTypeRequestDto modifyUserTypeRequestDto) {
        return adminService.modifyUserType(modifyUserTypeRequestDto);
    }

    @GetMapping("/prompt")
    @Operation(summary = "gpt 프롬프트 최신 조회", description = "gpt 프롬프트 최신 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getGptPrompt() {
        return adminService.getGptPrompt();
    }

    @PostMapping("/prompt")
    @Operation(summary = "gpt 프롬프트 저장", description = "gpt 프롬프트 저장", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> saveGptPrompt(@Valid @RequestBody SaveGptPromptRequestDto saveGptPromptRequestDto) {
        return adminService.saveGptPrompt(saveGptPromptRequestDto);
    }

    @PostMapping("/gpt")
    @Operation(summary = "gpt 내용 생성", description = "gpt 생성 후 질문 답변 로그 저장 및 답변 반환", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createGptContent(@RequestBody CreateGptContentRequestDto createGptContentRequestDto) {
        return adminService.createGptContent(createGptContentRequestDto);
    }
    @GetMapping("/gpt-log")
    @Operation(summary = "gpt 질문 답변 로그 확인", description = "gpt 질문 답변 로그 확인 / 필터 존재", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getGptLog(
            @RequestParam(name = "topicId", required = false) Long topicId,
            @RequestParam(name = "usertypeId", required = false) Long usertypeId
    ) {
        return adminService.getGptLog(topicId, usertypeId);
    }


    @PostMapping(value ="/img", consumes = { "multipart/form-data" })
    @Operation(summary = "이미지 저장", description = "이미지 s3 저장 후 s3 url 반환", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> saveImgToS3(@Valid @ModelAttribute SaveImgToS3RequestDto saveImgToS3RequestDto) {
        return adminService.saveImgToS3(saveImgToS3RequestDto);
    }

}
