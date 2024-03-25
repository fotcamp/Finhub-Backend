package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.repository.CategoryRepository;
import fotcamp.finhub.admin.repository.TopicRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.*;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.process.CategoryListProcessDto;
import fotcamp.finhub.main.dto.process.TopicListProcessDto;
import fotcamp.finhub.main.dto.request.ChangeNicknameRequestDto;
import fotcamp.finhub.main.dto.process.SearchResultListProcessDto;
import fotcamp.finhub.main.dto.response.*;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.main.repository.MemberScrapRepository;
import fotcamp.finhub.main.repository.PopularKeywordRepository;
import fotcamp.finhub.main.repository.RecentSearchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final MemberScrapRepository memberScrapRepository;
    private final PopularKeywordRepository popularKeywordRepository;
    private final RecentSearchRepository recentSearchRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> home(CustomUserDetails userDetails, int size){

        // 전체 카테고리리스트
        List<Category> allCategories = categoryRepository.findAllByOrderByIdAsc();
        List<CategoryListProcessDto> categoryListDtos = allCategories.stream()
                .map(category -> new CategoryListProcessDto(category.getId(), category.getName())).collect(Collectors.toList());

        // 첫번째 카테고리의 토픽 7개
        Category firstCategory = categoryRepository.findFirstByOrderByIdAsc();
        List<Topic> topicTop7 = topicRepository.findByCategoryAndIdGreaterThan(firstCategory, 0L, PageRequest.of(0, size));

        // 비로그인은 스크랩  전부 false
        //스크랩 유무 정보 제공
        List<TopicListProcessDto> topicListProcessDtoList = new ArrayList<>();
        for (Topic topic : topicTop7) {
            boolean isScrapped = false;
            if (userDetails != null) {
                // 로그인한 사용자의 경우, 스크랩 여부 확인
                Long memberId = userDetails.getMemberIdasLong();
                isScrapped = memberScrapRepository.findByMemberIdAndTopicId(memberId, topic.getId()).isPresent();
            }
            // TopicListProcessDto 객체 생성 및 리스트에 추가
            topicListProcessDtoList.add(new TopicListProcessDto(topic.getId(), topic.getTitle(), topic.getSummary(), isScrapped));
        }

        HomeResponseDto homeResponseDto = new HomeResponseDto(categoryListDtos, topicListProcessDtoList);
        return ResponseEntity.ok(ApiResponseWrapper.success(homeResponseDto));
    }
    
    public ResponseEntity<ApiResponseWrapper> changeNickname(CustomUserDetails userDetails , ChangeNicknameRequestDto dto){
        String newNickname = dto.getNewNickname();
        if (newNickname.length()>= 2 && newNickname.length() <= 10){
            Long memberId = userDetails.getMemberIdasLong();
            Member existingMember = memberRepository.findById(memberId).orElseThrow(
                    () -> new EntityNotFoundException("해당 요청 데이터가 존재하지 않습니다."));
            existingMember.updateNickname(newNickname);
            memberRepository.save(existingMember);
            return ResponseEntity.ok(ApiResponseWrapper.success("변경 완료"));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("변경조건에 맞게 작성하세요."));
        }
    }

    public ResponseEntity<ApiResponseWrapper> membershipResign(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member existingMember = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("해당 요청 데이터가 존재하지 않습니다."));
        memberRepository.delete(existingMember);
        return ResponseEntity.ok(ApiResponseWrapper.success("탈퇴 완료"));
    }

    public ResponseEntity<ApiResponseWrapper> search(CustomUserDetails userDetails, String method, String keyword, int pageSize, int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Topic> pageResult = null;
        switch (method) {
            case "title" -> {
                pageResult = topicRepository.findByTitleContaining(keyword, pageable);
                break;
            }
            case "summary" -> {
                pageResult = topicRepository.findBySummaryContaining(keyword, pageable);
                break;
            }
            case "both" -> {
                pageResult = topicRepository.findByTitleContainingOrSummaryContaining(keyword,keyword, pageable);
                break;
            }
            default -> throw new IllegalArgumentException("검색방법이 잘못되었습니다.");
        }
        if(userDetails != null){
            // 로그인 유저 최근검색어 데이터 추가 ( 중복되는 검색어는 최신으로 덮어쓰기, 최근검색어는 최대 10개까지만 저장하기 )
            Long memberId = userDetails.getMemberIdasLong();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
            RecentSearch findRecord = recentSearchRepository.findByMember_memberIdAndKeyword(memberId, keyword).orElse(new RecentSearch(member, keyword, LocalDateTime.now()));
            findRecord.updateRecord(LocalDateTime.now());
            recentSearchRepository.save(findRecord);

            // pagesize 11개로 요청 -> 11개가 온다면 제일 오래된 하나를 삭제
            PageRequest limit = PageRequest.of(0, 11, Sort.by(Sort.Direction.DESC, "localDateTime"));
            List<RecentSearch> recentSearchList = recentSearchRepository.findByMember_IdOrderByLocalDateTimeDesc(memberId, limit);
            if (recentSearchList.size() == 11){
                RecentSearch oldSearchData = recentSearchList.get(recentSearchList.size() - 1);
                System.out.println(oldSearchData.getKeyword());
                recentSearchRepository.delete(oldSearchData);
            }
        }

        List<Topic> resultList = pageResult.getContent();
        System.out.println("빈 문자열인가요?"+resultList);
        if(resultList.isEmpty()){ // 인기검색어 데이터는 검색 결과가 있을 때만 저장
            return ResponseEntity.ok(ApiResponseWrapper.success(resultList));
        }

        // 인기검색어 저장 로직
        PopularSearch popularSearch = popularKeywordRepository.findByKeyword(keyword).orElse(new PopularSearch(keyword));
        popularSearch.plusFrequency();
        popularKeywordRepository.save(popularSearch);

        List<SearchResultListProcessDto> response = resultList.stream()
                .map(topic -> new SearchResultListProcessDto(topic.getTitle(), topic.getSummary())).collect(Collectors.toList());
        SearchResponseDto responseDto = new SearchResponseDto(response, page, pageResult.getTotalPages(), pageResult.getTotalElements());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> otherCategories(CustomUserDetails userDetails, Long categoryId, int size){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException("카테고리가 존재하지 않습니다.")
        );
        //선택한 카테고리의 상위 토픽 7개
        List<Topic> topicTop7 = topicRepository.findByCategoryAndIdGreaterThan(findCategory, 0L, PageRequest.of(0, size));

        // 로그인 유무에 따라서 스크랩 정보 추가
        List<TopicListProcessDto> topicListProcessDtoList = new ArrayList<>();

        for(Topic topic : topicTop7){
            boolean isScrapped = false;
            if(userDetails != null){
                Long memberId = userDetails.getMemberIdasLong();
                isScrapped = memberScrapRepository.findByMemberIdAndTopicId(memberId, topic.getId()).isPresent();
            }
            String categoryName = findCategory.getName();
            // TopicListProcessDto 객체 생성 및 리스트에 추가
            topicListProcessDtoList.add(new TopicListProcessDto(topic.getId(), topic.getTitle(), topic.getSummary(), isScrapped, categoryName));
        }

        OtherCategoriesResponseDto responseDto = new OtherCategoriesResponseDto(topicListProcessDtoList);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> more(CustomUserDetails userDetails, Long categoryId, Long cursorId, int size){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException("카테고리가 존재하지 않습니다.")
        );
        // cursorId부터 다음 7개 토픽
        List<Topic> topicTop7 = topicRepository.findByCategoryAndIdGreaterThan(findCategory, cursorId, PageRequest.of(0, size));

        // 로그인 유무에 따라서 스크랩 정보 추가
        List<TopicListProcessDto> topicListProcessDtoList = new ArrayList<>();

        for(Topic topic : topicTop7){
            boolean isScrapped = false;
            if(userDetails != null){
                Long memberId = userDetails.getMemberIdasLong();
                isScrapped = memberScrapRepository.findByMemberIdAndTopicId(memberId, topic.getId()).isPresent();
            }
            String categoryName = findCategory.getName();
            // TopicListProcessDto 객체 생성 및 리스트에 추가
            topicListProcessDtoList.add(new TopicListProcessDto(topic.getId(), topic.getTitle(), topic.getSummary(), isScrapped, categoryName));
        }
        HomeMoreResponseDto responseDto = new HomeMoreResponseDto(topicListProcessDtoList);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> scrapTopic(CustomUserDetails userDetails, Long topicId){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("토픽ID가 존재하지 않습니다."));
        memberScrapRepository.save(new MemberScrap(member,topic));

        return ResponseEntity.ok(ApiResponseWrapper.success("스크랩 성공"));
    }

    public ResponseEntity<ApiResponseWrapper> recentSearch(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Pageable limit = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "localDateTime"));
        List<RecentSearch> recentSearchList = recentSearchRepository.findByMember_IdOrderByLocalDateTimeDesc(memberId, limit);
        List<RecentSearchResponseDto> responseDto = recentSearchList.stream()
                .map(recentSearch -> new RecentSearchResponseDto(recentSearch.getId(), recentSearch.getKeyword())).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> popularKeyword(){
        // order by frequency로 7개만 가져오기
        List<PopularSearch> popularSearchList = popularKeywordRepository.findTop7ByOrderByFrequencyDesc();
        List<PopularKeywordResponseDto> responseDto = popularSearchList.stream()
                .map(popularSearch -> new PopularKeywordResponseDto(popularSearch.getKeyword())).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

}
