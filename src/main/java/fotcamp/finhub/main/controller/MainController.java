package fotcamp.finhub.main.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.*;
import fotcamp.finhub.main.dto.response.thirdTab.SearchTopicResponseDto;
import fotcamp.finhub.main.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "main", description = "main api")
@RestController
@RequestMapping("/api/v1/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;



    // 단어 검색
    @GetMapping("/search/topic/{method}")
    @Operation(summary = "세 번째 탭 단어 검색", description = "제목만, 내용만, 제목+내용")
    public ResponseEntity<ApiResponseWrapper> searchTopic(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "method") String method,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "size", defaultValue = "3") int pageSize,
            @RequestParam(name = "page", defaultValue = "0") int page ){

        PageRequest pageable = PageRequest.of(page, pageSize);
        SearchTopicResponseDto searchTopicResult = mainService.searchTopic(userDetails, method, keyword, pageable);
        return ResponseEntity.ok(ApiResponseWrapper.success(searchTopicResult));
    }

    // 컬럼 검색
    @GetMapping("/search/column/{method}")
    @Operation(summary = "세 번째 탭 컬럼 검색", description = "제목만, 내용만, 제목+내용")
    public ResponseEntity<ApiResponseWrapper> searchColumn(
            @PathVariable(name = "method") String method,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "size", defaultValue = "2") int pageSize,
            @RequestParam(name = "page", defaultValue = "0") int page ){

        PageRequest pageable = PageRequest.of(page, pageSize);
        return mainService.searchColumn(method, keyword, pageable);
    }

    @GetMapping("/home/categoryList")
    @Operation(summary = "전체 카테고리 리스트", description = "전체 카테고리 리스트")
    public ResponseEntity<ApiResponseWrapper> categoryList(){
        return mainService.categoryList();
    }

    @GetMapping("/home/topicList")
    @Operation(summary = "토픽 7개씩 요청", description = "첫 번째 탭에서 사용할 기능")
    public ResponseEntity<ApiResponseWrapper> topicList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "categoryId", defaultValue = "1") Long categoryId,
            @RequestParam(name = "cursorId", defaultValue = "1") Long cursorId,
            @RequestParam(name = "size", defaultValue = "7") int size){
        return mainService.topicList(userDetails, categoryId, cursorId, size);
    }

    @PostMapping("/scrap")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "토픽 스크랩 설정 및 해제", description = "토픽 스크랩 설정 및 해제")
    public ResponseEntity<ApiResponseWrapper> scrapTopic(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ScrapTopicRequestDto dto){
        return mainService.scrapTopic(userDetails, dto);
    }

    @GetMapping("/topicInfo")
    @Operation(summary = "토픽 상세보기", description = "토픽 상세보기")
    public ResponseEntity<ApiResponseWrapper> topicInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "topicId") Long topicId) {
        return mainService.topicInfo(userDetails, topicId);
    }

    @GetMapping("/usertypeList")
    @Operation(summary = "유저 타입(직업) 리스트 요청", description = "유저 타입(직업) 리스트 요청")
    public ResponseEntity<ApiResponseWrapper> usertypeList(){
        return mainService.usertypeList();
    }

    @GetMapping("/gptContent")
    @Operation(summary = "직업, 토픽에 따른 gpt content 요청", description = "직업, 토픽에 따른 gpt content 요청")
    public ResponseEntity<ApiResponseWrapper> gptContent(
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "topicId") Long topicId,
            @RequestParam(name = "usertypeId") Long usertypeId ){
        return mainService.gptContent(categoryId, topicId, usertypeId);
    }

    // 다음 토픽
    @GetMapping("/nextTopic/{categoryId}/{topicId}")
    @Operation(summary = "다음 토픽 미리 보기", description = "다음 토픽 미리보기")
    public ResponseEntity<ApiResponseWrapper> nextTopic(
            @PathVariable(name = "categoryId") Long categoryId,
            @PathVariable(name = "topicId") Long topicId
    ){
        return mainService.nextTopic(categoryId, topicId);
    }

    // 최근검색 조회
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/recentKeyword")
    @Operation(summary = "최근 검색어 조회", description = "최근 검색어 조회")
    public ResponseEntity<ApiResponseWrapper> recentSearch(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return mainService.recentSearch(userDetails);
    }

    @GetMapping("/popularKeyword")
    @Operation(summary = "인기검색어 조회", description = "인기검색어 조회")
    public ResponseEntity<ApiResponseWrapper> popularKeyword(
    ){
        return mainService.popularKeyword();
    }

    // 최근검색 지우기
    @DeleteMapping("/recentKeyword")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "최근검색어 삭제하기", description = "최근검색어 삭제하기")
    public ResponseEntity<ApiResponseWrapper> deleteRecentKeyword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody DeleteRecentKeywordRequestDto dto
    ){
        return mainService.deleteRecentKeyword(userDetails, dto);
    }

    // 없는단어 요청하기
    @PostMapping("/search/keyword")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "없는 단어 요청하기", description = "없는 단어 요청하기")
    public ResponseEntity<ApiResponseWrapper> requestKeyword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody KeywordRequestDto dto){
        return mainService.requestKeyword(userDetails, dto);
    }

    // 닉네임 설정
    @PostMapping("/menu/setting/nickname")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 닉네임 변경", description = "닉네임 설정")
    public ResponseEntity<ApiResponseWrapper> changeNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChangeNicknameRequestDto dto){
        return mainService.changeNickname(userDetails, dto);
    }

    // 직업 목록
    @GetMapping("/menu/setting/usertype")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 직업 목록", description = "직업 목록")
    public ResponseEntity<ApiResponseWrapper> settingUsertype(){
        return mainService.usertypeList();
    }

    // 직업 설정
    @PostMapping("/menu/setting/usertype")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 유저 타입(직업) 선택", description = "직업 선택")
    public ResponseEntity<ApiResponseWrapper> selectJob(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody SelectUsertypeRequestDto dto){
        return mainService.selectUsertype(userDetails, dto);
    }

    // 두번째 탭 ( 선택한 카테고리에 대한 전체 토픽 리스트 )
    @GetMapping("/list")
    @Operation(summary = "전체 토픽 리스트", description = "전체 토픽 리스트")
    public ResponseEntity<ApiResponseWrapper> listTab(
            @RequestParam(name = "categoryId") Long categoryId){
        return mainService.list(categoryId);
    }

    // 아바타 목록
    @GetMapping("/menu/setting/avatar")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "유저 아바타 목록", description = "유저 아바타 목록")
    public ResponseEntity<ApiResponseWrapper> listAvatar(@AuthenticationPrincipal CustomUserDetails userDetails){
        return mainService.listAvatar(userDetails);
    }

    // 아바타 선택
    @PostMapping("/menu/setting/avatar")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "유저 아바타 선택", description = "유저 아바타 선택")
    public ResponseEntity<ApiResponseWrapper> selectAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SelectAvatarRequestDto dto){
        return mainService.selectAvatar(userDetails, dto);
    }

    // 아바타 지우기
    @DeleteMapping("/menu/setting/avatar")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "유저 아바타 지우기", description = "유저 아바타 지우기")
    public ResponseEntity<ApiResponseWrapper> deleteAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return mainService.deleteAvatar(userDetails);
    }
    // 스크랩 모음
    @GetMapping("/menu/myscrap")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "스크랩 모음 요청", description = "스크랩 모음")
    public ResponseEntity<ApiResponseWrapper> scrapList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return mainService.scrapList(userDetails);
    }

    @GetMapping("/menu/setting/resign")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 회원탈퇴", description = "회원 탈퇴 ")
    public ResponseEntity<ApiResponseWrapper> resignMembership(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return mainService.membershipResign(userDetails);
    }

    // 배너 리스트
    @GetMapping("/home/banner")
    @Operation(summary = "배너 리스트", description = "배너리스트")
    public ResponseEntity<ApiResponseWrapper> bannerList() {
        return mainService.bannerList();
    }
}

