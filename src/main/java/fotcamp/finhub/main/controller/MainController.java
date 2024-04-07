package fotcamp.finhub.main.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.*;
import fotcamp.finhub.main.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            @RequestParam(name = "size", defaultValue = "3") int size,
            @RequestParam(name = "page", defaultValue = "0") int page ){
        return mainService.searchTopic(userDetails, method, keyword, size,page);
    }

    @GetMapping("/home/categoryList")
    public ResponseEntity<ApiResponseWrapper> categoryList(){
        return mainService.categoryList();
    }

    @GetMapping("/home/topicList")
    public ResponseEntity<ApiResponseWrapper> topicList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "categoryId", defaultValue = "1") Long categoryId,
            @RequestParam(name = "cursorId", defaultValue = "1") Long cursorId,
            @RequestParam(name = "size", defaultValue = "7") int size){
        return mainService.topicList(userDetails, categoryId, cursorId, size);
    }

    @PostMapping("/scrap")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> scrapTopic(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ScrapTopicRequestDto dto){
        return mainService.scrapTopic(userDetails, dto);
    }

    @GetMapping("/topicInfo")
    public ResponseEntity<ApiResponseWrapper> topicInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "topicId") Long topicId) {
            return mainService.topicInfo(userDetails, topicId);
    }

    @GetMapping("/usertypeList")
    public ResponseEntity<ApiResponseWrapper> usertypeList(){
        return mainService.usertypeList();
    }

    @GetMapping("/gptContent")
    public ResponseEntity<ApiResponseWrapper> gptContent(
         @RequestParam(name = "categoryId") Long categoryId,
         @RequestParam(name = "topicId") Long topicId,
         @RequestParam(name = "usertypeId") Long usertypeId ){
        return mainService.gptContent(categoryId, topicId, usertypeId);
    }

    // 다음 토픽
    @GetMapping("/nextTopic/{categoryId}/{topicId}")
    public ResponseEntity<ApiResponseWrapper> nextTopic(
            @PathVariable(name = "categoryId") Long categoryId,
            @PathVariable(name = "topicId") Long topicId
    ){
        return mainService.nextTopic(categoryId, topicId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/thirdTab/recent")
    public ResponseEntity<ApiResponseWrapper> recentSearch(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return mainService.recentSearch(userDetails);
    }

    @GetMapping("/thirdTab/popular")
    public ResponseEntity<ApiResponseWrapper> popularKeyword(
    ){
        return mainService.popularKeyword();
    }

    @GetMapping("/thirdTab/delete/{searchId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> deleteRecentKeyword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "searchId") Long searchId
    ){
        return mainService.deleteRecentKeyword(userDetails, searchId);
    }

    @GetMapping("/thirdTab/deleteall")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> deleteAllRecentKeyword(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return mainService.deleteAllRecentKeyword(userDetails);
    }

    @PostMapping("/search/requestKeyword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> requestKeyword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NewKeywordRequestDto dto){
        return mainService.requestKeyword(userDetails, dto);
    }

    // 닉네임 설정
    @PostMapping("/menu/setting/nickname")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 닉네임 변경", description = "nickname change")
    public ResponseEntity<ApiResponseWrapper> changeNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChangeNicknameRequestDto dto){
        return mainService.changeNickname(userDetails, dto);
    }

    // 직업목록
    @GetMapping("/menu/setting/usertype")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> settingUsertype(){
        return mainService.usertypeList();
    }

    // 직업 설정
    @PostMapping("/menu/setting/usertype")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> selectJob(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody SelectUsertypeRequestDto dto){
        return mainService.selectUsertype(userDetails, dto);
    }

    // 두번째 탭 ( 선택한 카테고리에 대한 전체 토픽 리스트 )
    @GetMapping("/list")
    public ResponseEntity<ApiResponseWrapper> listTab(
            @RequestParam(name = "categoryId") Long categoryId){
        return mainService.list(categoryId);
    }

    // 아바타 목록
    @GetMapping("/menu/setting/avatar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> listAvatar(@AuthenticationPrincipal CustomUserDetails userDetails){
        return mainService.listAvatar(userDetails);
    }

    // 아바타 선택
    @PostMapping("/menu/setting/avatar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> selectAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SelectAvatarRequestDto dto){
        return mainService.selectAvatar(userDetails, dto);
    }

    // 아바타 지우기
    @DeleteMapping("/menu/setting/avatar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> deleteAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return mainService.deleteAvatar(userDetails);
    }
    // 스크랩 모음
    @GetMapping("/menu/myscrap")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> scrapList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return mainService.scrapList(userDetails);
    }

    @GetMapping("/menu/setting/resign")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 회원탈퇴", description = "membership resign")
    public ResponseEntity<ApiResponseWrapper> resignMembership(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return mainService.membershipResign(userDetails);
    }

    // 배너 리스트
    @GetMapping("/home/banner")
    public ResponseEntity<ApiResponseWrapper> bannerList() {
        return mainService.bannerList();
    }


    // 컬럼 검색
    @GetMapping("/search/column/{method}")
    @Operation(summary = "세 번째 탭 컬럼 검색", description = "제목만, 내용만, 제목+내용")
    public ResponseEntity<ApiResponseWrapper> searchColumn(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "method") String method,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "size", defaultValue = "2") int size,
            @RequestParam(name = "page", defaultValue = "0") int page ){
        return mainService.searchColumn(userDetails, method, keyword, size,page);
    }


}

