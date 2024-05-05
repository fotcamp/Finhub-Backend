package fotcamp.finhub.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Api;
import fotcamp.finhub.admin.dto.request.*;
import fotcamp.finhub.admin.service.AdminService;
import fotcamp.finhub.admin.service.FcmService;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.utils.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

@Tag(name = "E admin", description = "admin api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final FcmService fcmService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인 요청", description = "관리자 여부 판단")
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
    @Operation(summary = "카테고리 전체 조회", description = "category 전체 조회")
    public ResponseEntity<ApiResponseWrapper> getAllCategory(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYN", required = false) String useYN) {

        if (useYN != null && !useYN.equals("Y") && !useYN.equals("N")) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("useYN 형식 오류"));
        }
        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllCategory(pageable, useYN);
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "카테고리 상세 조회", description = "category 상세 조회")
    public ResponseEntity<ApiResponseWrapper> getDetailCategory(@PathVariable("categoryId") Long categoryId) {
        return adminService.getDetailCategory(categoryId);
    }

    @PostMapping(value = "/category")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "카테고리 생성", description = "category 등록")
    public ResponseEntity<ApiResponseWrapper> createCategory(@Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        return adminService.createCategory(createCategoryRequestDto);
    }

    @PutMapping("/category")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "카테고리 수정", description = "category 수정")
    public ResponseEntity<ApiResponseWrapper> modifyCategory(@Valid @RequestBody ModifyCategoryRequestDto modifyCategoryRequestDto) {
        return adminService.modifyCategory(modifyCategoryRequestDto);
    }

    @GetMapping("/topic")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "토픽 전체 조회", description = "topic 전체 조회")
    public ResponseEntity<ApiResponseWrapper> getAllTopic(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "categoryId", required = false) Long id,
            @RequestParam(name = "useYN", required = false) String useYN
    ) {

        if (useYN != null && !useYN.equals("Y") && !useYN.equals("N")) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("useYN 형식 오류"));
        }
        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllTopic(pageable, id, useYN);
    }

    @GetMapping("/topic/{topicId}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "토픽 상세조회", description = "topic 상세 조회")
    public ResponseEntity<ApiResponseWrapper> getDetailTopic(@PathVariable(name = "topicId") Long topicId) {
        return adminService.getDetailTopic(topicId);
    }

    @PostMapping("/topic")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "토픽 생성", description = "topic 등록")
    public ResponseEntity<ApiResponseWrapper> createTopic(
            @Valid @RequestBody CreateTopicRequestDto createTopicRequestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createTopic(createTopicRequestDto, userDetails);
    }

    @PutMapping("/topic")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "토픽 수정", description = "topic 수정")
    public ResponseEntity<ApiResponseWrapper> modifyTopic(@Valid @RequestBody ModifyTopicRequestDto modifyTopicRequestDto,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.modifyTopic(modifyTopicRequestDto, userDetails);
    }

    @GetMapping("/usertype")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "유저 타입 전체조회", description = "usertype 전체 조회")
    public ResponseEntity<ApiResponseWrapper> getAllUserType(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYN", required = false) String useYN
    ) {
        if (useYN != null && !useYN.equals("Y") && !useYN.equals("N")) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("useYN 형식 오류"));
        }

        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllUserType(pageable, useYN);
    }

    @GetMapping("/usertype/{typeId}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "유저 타입 상세조회", description = "usertype 상세 조회")
    public ResponseEntity<ApiResponseWrapper> getDetailUserType(@PathVariable(name = "typeId") Long typeId) {
        return adminService.getDetailUserType(typeId);
    }

    @PostMapping("/usertype")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "유저 타입 생성", description = "usertype 등록")
    public ResponseEntity<ApiResponseWrapper> createUserType(@Valid @RequestBody CreateUserTypeRequestDto createUserTypeRequestDto) {
        return adminService.createUserType(createUserTypeRequestDto);
    }

    @PutMapping("/usertype")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "유저 타입 수정", description = "usertype 수정")
    public ResponseEntity<ApiResponseWrapper> modifyUserType(@Valid @RequestBody ModifyUserTypeRequestDto modifyUserTypeRequestDto) {
        return adminService.modifyUserType(modifyUserTypeRequestDto);
    }

    @GetMapping("/prompt")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "gpt 프롬프트 최신 조회", description = "gpt 프롬프트 최신 조회")
    public ResponseEntity<ApiResponseWrapper> getGptPrompt() {
        return adminService.getGptPrompt();
    }

    @PostMapping("/prompt")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "gpt 프롬프트 저장", description = "gpt 프롬프트 저장")
    public ResponseEntity<ApiResponseWrapper> saveGptPrompt(@Valid @RequestBody SaveGptPromptRequestDto saveGptPromptRequestDto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.saveGptPrompt(saveGptPromptRequestDto, userDetails);
    }

    @PostMapping("/topic-summary")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "토픽 요약 gpt 내용 생성", description = "토픽 요약 gpt 생성 후 답변 반환")
    public ResponseEntity<ApiResponseWrapper> createTopicSummaryGptContent(@RequestBody CreateTopicSummaryGptContentRequestDto createTopicSummaryGptContentRequestDto,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createTopicSummaryGptContent(createTopicSummaryGptContentRequestDto, userDetails);
    }

    @PostMapping("/topic-usertype")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "토픽 유저타입 gpt 내용 생성", description = "토픽 유저타입 gpt 내용 생성 후 질문 답변 로그 저장 및 답변 반환")
    public ResponseEntity<ApiResponseWrapper> createTopicUsertypeGptContent(@RequestBody CreateGptContentRequestDto createGptContentRequestDto,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createTopicUsertypeGptContent(createGptContentRequestDto, userDetails);
    }
    @GetMapping("/gpt-log")
    @PreAuthorize("hasRole('SUPER')")
    @Operation(summary = "gpt 질문 답변 로그 확인", description = "gpt 질문 답변 로그 확인 / 필터 존재")
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
    @Operation(summary = "이미지 저장", description = "이미지 s3 저장 후 이미지 s3 url 반환")
    public ResponseEntity<ApiResponseWrapper> saveImgToS3(@Valid @ModelAttribute SaveImgToS3RequestDto saveImgToS3RequestDto) {
        return adminService.saveImgToS3(saveImgToS3RequestDto);
    }

    @PostMapping(value = "/quiz")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "퀴즈 생성", description = "퀴즈 날짜에 맞게 생성")
    public ResponseEntity<ApiResponseWrapper> createQuiz(@Valid @RequestBody CreateQuizRequestDto createQuizRequestDto,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createQuiz(createQuizRequestDto, userDetails);
    }

    @GetMapping(value = "/quiz/{year}/{month}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "퀴즈 월 전체 조회", description = "퀴즈 월 전체 조회 기능")
    public ResponseEntity<ApiResponseWrapper> getMonthlyQuiz(@PathVariable(name = "year") String year,
                                                             @PathVariable(name = "month") String month) {
        if (!(year.length() == 4) || !(1 <= month.length() && month.length() <= 2) || Long.parseLong(month) < 1 || Long.parseLong(month) > 12) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("날짜 형식을 확인해주세요"));
        }
        return adminService.getMonthlyQuiz(year, month);
    }

    @GetMapping(value = "/quiz/{year}/{month}/{day}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "퀴즈 일 상세 조회", description = "퀴즈 일 상세 조회 기능")
    public ResponseEntity<ApiResponseWrapper> getDailyQuiz(@PathVariable(name = "year") String year,
                                                           @PathVariable(name = "month") String month,
                                                           @PathVariable(name = "day") String day) {
        if (!(year.length() == 4) || !(1 <= month.length() && month.length() <= 2) || !(1 <= day.length() && day.length() <= 2) || Long.parseLong(month) < 1 || Long.parseLong(month) > 12 || Long.parseLong(day) < 1 || Long.parseLong(day) > 31) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("날짜 형식을 확인해주세요"));
        }
        return adminService.getDailyQuiz(year, month, day);
    }

    @PutMapping(value = "/quiz")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "퀴즈 수정", description = "퀴즈 수정 기능")
    public ResponseEntity<ApiResponseWrapper> modifyQuiz(@Valid @RequestBody ModifyQuizRequestDto modifyQuizRequestDto,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.modifyQuiz(modifyQuizRequestDto, userDetails);
    }

    @PostMapping(value = "/banner")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "배너 생성", description = "배너 생성 기능")
    public ResponseEntity<ApiResponseWrapper> createBanner(@RequestBody CreateBannerRequestDto createBannerRequestDto,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createBanner(createBannerRequestDto, userDetails);
    }

    @PutMapping(value = "/banner")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "배너 수정", description = "배너 수정 기능")
    public ResponseEntity<ApiResponseWrapper> modifyBanner(@RequestBody ModifyBannerRequestDto modifyBannerRequestDto,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.modifyBanner(modifyBannerRequestDto, userDetails);
    }

    @GetMapping("/banner")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "배너 전체조회", description = "배너 전체 조회")
    public ResponseEntity<ApiResponseWrapper> getAllBanner(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYN", required = false) String useYN
    ) {
        if (useYN != null && !useYN.equals("Y") && !useYN.equals("N")) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("useYN 형식 오류"));
        }

        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getAllBanner(pageable, useYN);
    }

    @GetMapping("/banner/{bannerId}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "배너 상세조회", description = "배너 상세 조회")
    public ResponseEntity<ApiResponseWrapper> getDetailBanner(@PathVariable(name = "bannerId") Long bannerId) {
        return adminService.getDetailBanner(bannerId);
    }

    @PostMapping(value = "/img/delete")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "생성/수정 취소 시 이미지 삭제", description = "s3에 저장한 이미지 삭제")
    public ResponseEntity<ApiResponseWrapper> deleteImg(@RequestBody DeleteS3ImageRequestDto deleteS3ImageRequestDto) {
        return adminService.deleteS3Image(deleteS3ImageRequestDto);
    }

    @GetMapping(value = "/no-word")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "없는 단어 요청 확인하기", description = "없는 단어 요청 확인하기")
    public ResponseEntity<ApiResponseWrapper> getNoWordList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "resolvedYN", required = false) String resolvedYN
    ) {
        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getNoWordList(pageable, resolvedYN);
    }

    @PostMapping(value = "/no-word")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "없는 단어 요청 확인 시 체크하기", description = "없는 단어 요청 확인 시 체크하기")
    public ResponseEntity<ApiResponseWrapper> checkNoWord(@RequestBody CheckNoWordRequestDto checkNoWordRequestDto) {
        return adminService.checkNoWord(checkNoWordRequestDto);
    }

    @PostMapping(value = "/user-avatar")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "유저 아바타 생성", description = "유저 아바타 생성하기")
    public ResponseEntity<ApiResponseWrapper> createUserAvatar(@RequestBody CreateUserAvatarRequestDto createUserAvatarRequestDto,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createUserAvatar(createUserAvatarRequestDto, userDetails);
    }

    @GetMapping(value = "/user-avatar")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "유저 아바타 전체 조회", description = "유저 아바타 전체 조회")
    public ResponseEntity<ApiResponseWrapper> getUserAvatar() {
        return adminService.getUserAvatar();
    }

    @PostMapping(value = "/calendar-emoticon")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "달력 이모티콘 생성", description = "달력 이모티콘 생성하기")
    public ResponseEntity<ApiResponseWrapper> createCalendarEmoticon(@RequestBody CreateCalendarEmoticonRequestDto createCalendarEmoticonRequestDto,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createCalendarEmoticon(createCalendarEmoticonRequestDto, userDetails);
    }

    @GetMapping(value = "/calendar-emoticon")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "달력 이모티콘 전체 조회", description = "달력 이모티콘 전체 조회")
    public ResponseEntity<ApiResponseWrapper> getCalendarEmoticon() {
        return adminService.getCalendarEmoticon();
    }

    @PostMapping(value = "/gpt-column/content")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "GPT COLUMN 내용 생성", description = "GPT COLUMN 내용 생성")
    public ResponseEntity<ApiResponseWrapper> creteGptColumnContent(@RequestBody CreateGptColumnRequestDto createGptColumnRequestDto) {
        return adminService.createGptColumnContent(createGptColumnRequestDto);
    }

    @PostMapping(value = "/gpt-column/summary")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "GPT COLUMN 요약 생성", description = "GPT COLUMN 요약 생성")
    public ResponseEntity<ApiResponseWrapper> createGptColumnSummary(@RequestBody CreateGptColumnRequestDto createGptColumnRequestDto) {
        return adminService.createGptColumnSummary(createGptColumnRequestDto);
    }

    @PostMapping(value = "/gpt-column")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "GPT COLUMN 생성", description = "GPT COLUMN 생성")
    public ResponseEntity<ApiResponseWrapper> createGptColumn(@RequestBody SaveGptColumnRequestDto saveGptColumnRequestDto,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.createGptColumn(saveGptColumnRequestDto, userDetails);
    }

    @GetMapping(value = "/gpt-column")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "GPT COLUMN 전체 조회", description = "GPT COLUMN 전체 조회")
    public ResponseEntity<ApiResponseWrapper> getGptColumn() {
        return adminService.getGptColumn();
    }

    @GetMapping(value = "/gpt-column/{id}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "GPT COLUMN 상세 조회", description = "GPT COLUMN 상세 조회")
    public ResponseEntity<ApiResponseWrapper> getDetailGptColumn(@PathVariable(name = "id") Long id) {
        return adminService.getDetailGptColumn(id);
    }

    @PutMapping(value = "/gpt-column")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "GPT COLUMN 수정", description = "GPT COLUMN 수정")
    public ResponseEntity<ApiResponseWrapper> modifyGptColumn(@Valid @RequestBody ModifyGptColumnRequestDto modifyGptColumnRequestDto,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        return adminService.modifyGptColumn(modifyGptColumnRequestDto, userDetails);
    }

    @PostMapping("/announce")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "공지사항 생성", description = "공지사항")
    public ResponseEntity<ApiResponseWrapper> createAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateAnnounceRequestDto dto)
    {
        return adminService.createAnnouncement(userDetails, dto);
    }

    @PutMapping("/announce")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "공지사항 수정", description = "공지사항")
    public ResponseEntity<ApiResponseWrapper> updateAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ModifyAnnounceRequestDto dto
    ){
        return adminService.updateAnnouncement(userDetails, dto);
    }

    @DeleteMapping("/announce")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "공지사항 삭제", description = "공지사항")
    public ResponseEntity<ApiResponseWrapper> deleteAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DeleteAnnounceRequestDto dto
    ){
        return adminService.deleteAnnouncement(userDetails, dto);
    }

    @GetMapping("/report/reason")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "신고사유 조회", description = "신고사유 조회")
    public ResponseEntity<ApiResponseWrapper> commentReasons(){
        return adminService.commentReasons();
    }

    @PostMapping("/report/reason")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "신고사유 등록", description = "신고사유 등록하기")
    public ResponseEntity<ApiResponseWrapper> registerReportReason(
            @Valid @RequestBody ReportReasonRequestDto dto
    ){
        return adminService.registerReportReason(dto);
    }

    @PutMapping("/report/reason")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "신고사유 수정", description = "신고사유 수정하기")
    public ResponseEntity<ApiResponseWrapper> modifyReportReason(
            @Valid @RequestBody ReportReasonModifyRequestDto dto
    ){
        return adminService.modifyReportReason(dto);
    }

    @PostMapping("/fcm-token")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "fcmToken 저장하기", description = "fcm 토큰 manager table에 저장하기")
    public ResponseEntity<ApiResponseWrapper> saveFcmToken(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SaveFcmTokenRequestDto dto
    ){
        return adminService.saveFcmToken(userDetails, dto);
    }

    @PostMapping("/send-noti")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE')")
    @Operation(summary = "알림메시지 전송", description = "타입별 알림메시지 구분 전송")
    public ResponseEntity<ApiResponseWrapper> sendNotification(
            @Valid @RequestBody CreateFcmMessageRequestDto dto
            ) throws JsonProcessingException {
        return fcmService.sendFcmNotifications(dto);
    }
    @GetMapping("/announce")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "공지사항 조회", description = "admin 누구나 공지사항 조회 가능")
    public ResponseEntity<ApiResponseWrapper> getAnnounceList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ){

        return adminService.getAnnounceList(page, pageSize);
    }

    @GetMapping("/autoLogin")
    @Operation(summary = "관리자용 자동로그인", description = "관리자용 자동로그인")
    public ResponseEntity<ApiResponseWrapper> adminAutoLogin(HttpServletRequest request){
        return adminService.adminAutoLogin(request);
    }

    @GetMapping("/announce/{id}")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상세 조회 ")
    public ResponseEntity<ApiResponseWrapper> getAnnounceDetail(
            @PathVariable(name = "id") Long id
    ){
        return adminService.getAnnounceDetail(id);
    }

    @GetMapping("/report/comment")
    @PreAuthorize("hasRole('SUPER') or hasRole('BE') or hasRole('FE')")
    @Operation(summary = "신고된 댓글 보기", description = "신고된 댓글 조회 ")
    public ResponseEntity<ApiResponseWrapper> getReportedComment(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                 @RequestParam(name = "useYN", required = false) String useYN
    ) {
        Pageable pageable = PageableUtil.createPageableWithDefaultSort(page, size, "id");
        return adminService.getReportedComment(pageable, useYN);
    }
}
