package fotcamp.finhub.admin.controller;

import fotcamp.finhub.admin.dto.request.*;
import fotcamp.finhub.admin.service.AdminService;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.utils.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "카테고리 전체 조회", description = "category 전체 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getAllCategory(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYN", required = false) String useYN) {

        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllCategory(pageable, useYN);
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "카테고리 상세 조회", description = "category 상세 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getDetailCategory(@PathVariable("categoryId") Long categoryId) {
        return adminService.getDetailCategory(categoryId);
    }

    @PostMapping(value = "/category")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "카테고리 생성", description = "category 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createCategory(@Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        return adminService.createCategory(createCategoryRequestDto);
    }

    @PutMapping("/category")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "카테고리 수정", description = "category 수정", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyCategory(@Valid @RequestBody ModifyCategoryRequestDto modifyCategoryRequestDto) {
        return adminService.modifyCategory(modifyCategoryRequestDto);
    }

    @GetMapping("/topic")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
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
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "토픽 상세조회", description = "topic 상세 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getDetailTopic(@PathVariable(name = "topicId") Long topicId) {
        return adminService.getDetailTopic(topicId);
    }

    @PostMapping("/topic")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "토픽 생성", description = "topic 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createTopic(
            @Valid @RequestBody CreateTopicRequestDto createTopicRequestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createTopic(createTopicRequestDto, userDetails);
    }

    @PutMapping("/topic")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "토픽 수정", description = "topic 수정", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyTopic(@Valid @RequestBody ModifyTopicRequestDto modifyTopicRequestDto,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.modifyTopic(modifyTopicRequestDto, userDetails);
    }

    @GetMapping("/usertype")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "유저 타입 전체조회", description = "usertype 전체 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getAllUserType(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYN", required = false) String useYN
    ) {
        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllUserType(pageable, useYN);
    }

    @GetMapping("/usertype/{typeId}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "유저 타입 상세조회", description = "usertype 상세 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getDetailUserType(@PathVariable(name = "typeId") Long typeId) {
        return adminService.getDetailUserType(typeId);
    }

    @PostMapping("/usertype")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "유저 타입 생성", description = "usertype 등록", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createUserType(@Valid @RequestBody CreateUserTypeRequestDto createUserTypeRequestDto) {
        return adminService.createUserType(createUserTypeRequestDto);
    }

    @PutMapping("/usertype")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "유저 타입 수정", description = "usertype 수정", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyUserType(@Valid @RequestBody ModifyUserTypeRequestDto modifyUserTypeRequestDto) {
        return adminService.modifyUserType(modifyUserTypeRequestDto);
    }

    @GetMapping("/prompt")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "gpt 프롬프트 최신 조회", description = "gpt 프롬프트 최신 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getGptPrompt() {
        return adminService.getGptPrompt();
    }

    @PostMapping("/prompt")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "gpt 프롬프트 저장", description = "gpt 프롬프트 저장", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> saveGptPrompt(@Valid @RequestBody SaveGptPromptRequestDto saveGptPromptRequestDto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.saveGptPrompt(saveGptPromptRequestDto, userDetails);
    }

    @PostMapping("/gpt")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "gpt 내용 생성", description = "gpt 생성 후 질문 답변 로그 저장 및 답변 반환", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createGptContent(@RequestBody CreateGptContentRequestDto createGptContentRequestDto,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createGptContent(createGptContentRequestDto, userDetails);
    }
    @GetMapping("/gpt-log")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "gpt 질문 답변 로그 확인", description = "gpt 질문 답변 로그 확인 / 필터 존재", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getGptLog(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "topicId", required = false) Long topicId,
            @RequestParam(name = "usertypeId", required = false) Long usertypeId
    ) {
        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getGptLog(pageable, topicId, usertypeId);
    }


    @PostMapping(value ="/img", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "이미지 저장", description = "이미지 s3 저장 후 이미지 s3 url 반환", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> saveImgToS3(@Valid @ModelAttribute SaveImgToS3RequestDto saveImgToS3RequestDto) {
        return adminService.saveImgToS3(saveImgToS3RequestDto);
    }

    @PostMapping(value = "/quiz")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "퀴즈 생성", description = "퀴즈 날짜에 맞게 생성", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createQuiz(@Valid @RequestBody CreateQuizRequestDto createQuizRequestDto,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createQuiz(createQuizRequestDto, userDetails);
    }

    @GetMapping(value = "/quiz/{year}/{month}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "퀴즈 월 전체 조회", description = "퀴즈 월 전체 조회 기능", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getMonthlyQuiz(@PathVariable(name = "year") Long year,
                                                             @PathVariable(name = "month") Long month) {
        return adminService.getMonthlyQuiz(year, month);
    }

    @GetMapping(value = "/quiz/{year}/{month}/{day}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "퀴즈 일 상세 조회", description = "퀴즈 일 상세 조회 기능", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getDailyQuiz(@PathVariable(name = "year") Long year,
                                                           @PathVariable(name = "month") Long month,
                                                           @PathVariable(name = "day") Long day) {
        return adminService.getDailyQuiz(year, month, day);
    }

    @PutMapping(value = "/quiz")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "퀴즈 수정", description = "퀴즈 수정 기능", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyQuiz(@Valid @RequestBody ModifyQuizRequestDto modifyQuizRequestDto,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.modifyQuiz(modifyQuizRequestDto, userDetails);
    }

    @PostMapping(value = "/banner")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "배너 생성", description = "배너 생성 기능", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> createBanner(@RequestBody CreateBannerRequestDto createBannerRequestDto,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createBanner(createBannerRequestDto, userDetails);
    }

    @PutMapping(value = "/banner")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "배너 수정", description = "배너 수정 기능", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> modifyBanner(@RequestBody ModifyBannerRequestDto modifyBannerRequestDto,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.modifyBanner(modifyBannerRequestDto, userDetails);
    }

    @GetMapping("/banner")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "배너 전체조회", description = "배너 전체 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getAllBanner(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYN", required = false) String useYN
    ) {
        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllBanner(pageable, useYN);
    }

    @GetMapping("/banner/{bannerId}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "배너 상세조회", description = "배너 상세 조회", tags = {"AdminController"})
    public ResponseEntity<ApiResponseWrapper> getDetailBanner(@PathVariable(name = "bannerId") Long bannerId) {
        return adminService.getDetailBanner(bannerId);
    }
}
