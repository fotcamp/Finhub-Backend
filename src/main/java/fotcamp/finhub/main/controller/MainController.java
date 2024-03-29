package fotcamp.finhub.main.controller;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.ChangeNicknameRequestDto;
import fotcamp.finhub.main.dto.request.NewKeywordRequestDto;
import fotcamp.finhub.main.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PostMapping("/setting/nickname")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 닉네임 변경", description = "nickname change")
    public ResponseEntity<ApiResponseWrapper> changeNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChangeNicknameRequestDto dto){
        return mainService.changeNickname(userDetails, dto);
    }

    @GetMapping("/setting/resign")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "설정 - 회원탈퇴", description = "membership resign")
    public ResponseEntity<ApiResponseWrapper> resignMembership(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return mainService.membershipResign(userDetails);
    }

    // 검색
    @GetMapping("/search/{method}")
    @Operation(summary = "세 번째 탭 검색", description = "제목만, 내용만, 제목+내용")
    public ResponseEntity<ApiResponseWrapper> search(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "method") String method,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "size", defaultValue = "4") int size,
            @RequestParam(name = "page", defaultValue = "0") int page ){
        return mainService.search(userDetails, method, keyword, size,page);
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponseWrapper> home(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "size", defaultValue = "7") int size
    ){return mainService.home(userDetails, size);}

    @GetMapping("/home/{categoryId}")
    public ResponseEntity<ApiResponseWrapper> otherCategories(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestParam(name = "size", defaultValue = "7") int size
    ){
        return mainService.otherCategories(userDetails, categoryId, size);
    }

    @GetMapping("/home/more/{categoryId}")
    public ResponseEntity<ApiResponseWrapper> more(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestParam(name = "cursorId") Long cursorId,
            @RequestParam(name = "size", defaultValue = "7") int size
    ){
        return mainService.more(userDetails, categoryId, cursorId, size);
    }

    @GetMapping("/home/scrap/{topicId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseWrapper> scrapTopic(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "topicId") Long topicId
    ){
        return mainService.scrapTopic(userDetails, topicId);
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
}
