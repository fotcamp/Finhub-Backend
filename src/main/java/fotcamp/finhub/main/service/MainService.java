package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.repository.*;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.*;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.main.dto.process.*;
import fotcamp.finhub.main.dto.process.secondTab.*;
import fotcamp.finhub.main.dto.process.thirdTab.SearchColumnResultListProcessDto;
import fotcamp.finhub.main.dto.process.thirdTab.SearchPageInfoProcessDto;
import fotcamp.finhub.main.dto.process.thirdTab.SearchTopicResultListProcessDto;
import fotcamp.finhub.main.dto.request.*;
import fotcamp.finhub.main.dto.response.*;
import fotcamp.finhub.main.dto.response.firstTab.BannerListResponseDto;
import fotcamp.finhub.main.dto.response.firstTab.CategoryListResponseDto;
import fotcamp.finhub.main.dto.response.firstTab.TopicListResponseDto;
import fotcamp.finhub.main.dto.response.popularSearch.PopularSearchDto;
import fotcamp.finhub.main.dto.response.popularSearch.PopularSearchResponseDto;
import fotcamp.finhub.main.dto.response.secondTab.*;
import fotcamp.finhub.main.dto.response.thirdTab.RecentSearchResponseDto;
import fotcamp.finhub.main.dto.response.thirdTab.SearchColumnResponseDto;
import fotcamp.finhub.main.dto.response.thirdTab.SearchTopicResponseDto;
import fotcamp.finhub.main.repository.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final TopicRequestRepository topicRequestRepository;
    private final UserTypeRepository userTypeRepository;
    private final GptRepository gptRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final BannerRepository bannerRepository;
    private final GptColumnRepository gptColumnRepository;
    private final WeekPopularKeywordRepository weekPopularKeywordRepository;
    private final PostsScrapRepository postsScrapRepository;
    private final AnnouncementRepository announcementRepository;
    private final CommentsRepository commentsRepository;
    private final AwsS3Service awsS3Service;
    private final PostsLikeRepository postsLikeRepository;
    private static final int MAX_RECENT_SEARCHES = 10;

    // 전체 카테고리 리스트
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> categoryList(){
        List<Category> allCategories = categoryRepository.findAllByUseYNOrderByIdAsc("Y");
        List<CategoryListProcessDto> categoryList = allCategories.stream()
                .map(category -> new CategoryListProcessDto(category.getId(), category.getName())).collect(Collectors.toList());
        CategoryListResponseDto responseDto = new CategoryListResponseDto(categoryList);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    // 토픽 리스트
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> topicList(CustomUserDetails userDetails, Long categoryId, Long cursorId, int size){
        // 요청받은 카테고리의 토픽 7개
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));
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
            TopicListProcessDto processDto = TopicListProcessDto.builder()
                    .topicId(topic.getId())
                    .title(topic.getTitle())
                    .summary(topic.getSummary())
                    .categoryName(categoryName)
                    .isScrapped(isScrapped)
                    .img_path(awsS3Service.combineWithBaseUrl(topic.getThumbnailImgPath())).build();
            topicListProcessDtoList.add(processDto);
        }
        return ResponseEntity.ok(ApiResponseWrapper.success(new TopicListResponseDto(topicListProcessDtoList)));
    }
    
    public ResponseEntity<ApiResponseWrapper> changeNickname(CustomUserDetails userDetails , ChangeNicknameRequestDto dto){
        String newNickname = dto.getNickname();
        if (memberRepository.existsByNickname(newNickname)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("이미 존재하는 닉네임입니다."));
        }
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        member.updateNickname(newNickname);
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> membershipResign(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member existingMember = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("해당 멤버는 존재하지 않습니다."));
        memberRepository.delete(existingMember);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public SearchTopicResponseDto searchTopic(CustomUserDetails userDetails, String method, String keyword, Pageable pageable){
        Page<Topic> pageResult = null;
        switch (method) {
            case "title" -> {
                pageResult = topicRepository.findByUseYNAndTitleContaining("Y", keyword, pageable);
            }
            case "summary" -> {
                pageResult = topicRepository.findByUseYNAndSummaryContaining("Y", keyword, pageable);
            }
            case "both" -> {
                pageResult = topicRepository.findByUseYNAndTitleContainingOrSummaryContaining("Y", keyword,keyword, pageable);
            }
            default -> throw new IllegalArgumentException("검색방법이 잘못되었습니다.");
        }
        // 인기검색어 카운트 기능 ( 해당 키워드로 첫 검색 상황 )
        if (pageable.getPageNumber() == 0){
            incrementPopularKeyword(keyword);
        }
        // 최근검색 기능 ( 로그인 유저 + 해당 키워드로 첫 검색 상황 )
        if (userDetails != null && pageable.getPageNumber() == 0) {
            handleRecentSearch(userDetails.getMemberIdasLong(), keyword);
        }

        List<SearchTopicResultListProcessDto> searchResultProcessDto = pageResult.stream().map(topic -> SearchTopicResultListProcessDto.builder()
                .topicId(topic.getId())
                .categoryId(topic.getCategory().getId())
                .title(topic.getTitle())
                .summary(topic.getSummary())
                .build()).collect(Collectors.toList());
        SearchPageInfoProcessDto pageInfoProcessDto = SearchPageInfoProcessDto.builder()
                .currentPage(pageable.getPageNumber())
                .totalPages(pageResult.getTotalPages())
                .totalResults(pageResult.getTotalElements()).build();
        return new SearchTopicResponseDto(searchResultProcessDto, pageInfoProcessDto);
    }

    public void handleRecentSearch(Long memberId, String keyword){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        RecentSearch recentSearch = recentSearchRepository.findByMemberAndKeyword(member, keyword)
                    .orElse(new RecentSearch(member, keyword, LocalDateTime.now()));
        recentSearch.updateRecord(LocalDateTime.now());
        recentSearchRepository.save(recentSearch);

        List<RecentSearch> recentSearchList = recentSearchRepository.findByMemberOrderByLocalDateTimeDesc(member);
        if (recentSearchList.size() > MAX_RECENT_SEARCHES){ // 최대 10개까지만 최근검색키워드 저장
            recentSearchRepository.delete(recentSearchList.get(recentSearchList.size() - 1));
        }
    }

    public void incrementPopularKeyword(String keyword){
        Optional<PopularSearch> popularSearch = popularKeywordRepository.findByKeywordAndDate(keyword, LocalDate.now());
        if (popularSearch.isPresent()) {
            popularSearch.get().plusFrequency();
        } else {
            PopularSearch newKeyword = PopularSearch.builder()
                    .keyword(keyword)
                    .build();
            popularKeywordRepository.save(newKeyword);
        }
    }

    // 컬럼 검색
    public ResponseEntity<ApiResponseWrapper> searchColumn(String method, String keyword, Pageable pageable) {
        Page<GptColumn> pageResult = null;
        switch (method) {
            case "title" -> {
                pageResult = gptColumnRepository.findByUseYNAndTitleContaining("Y", keyword, pageable);
            }
            case "content" -> {
                pageResult = gptColumnRepository.findByUseYNAndContentContaining("Y", keyword, pageable);
            }
            case "both" -> {
                pageResult = gptColumnRepository.findByUseYNAndTitleContainingOrContentContaining("Y", keyword, keyword, pageable);
            }
            default -> throw new IllegalArgumentException("검색방법이 잘못되었습니다.");
        }
        List<GptColumn> resultList = pageResult.getContent();
        List<SearchColumnResultListProcessDto> processDtoList = resultList.stream()
                .map(gptColumn -> SearchColumnResultListProcessDto.builder()
                        .id(gptColumn.getId())
                        .title(gptColumn.getTitle())
                        .content(gptColumn.getContent())
                        .build())
                .collect(Collectors.toList());

        SearchPageInfoProcessDto pageInfoProcessDto = SearchPageInfoProcessDto.builder()
                .currentPage(pageable.getPageNumber())
                .totalPages(pageResult.getTotalPages())
                .totalResults(pageResult.getTotalElements()).build();
        SearchColumnResponseDto responseDto = new SearchColumnResponseDto(processDtoList, pageInfoProcessDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> scrap(CustomUserDetails userDetails, ScrapRequestDto dto){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        if (dto.getType() == 1) { // 토픽 스크랩
            Topic topic = topicRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("토픽ID가 존재하지 않습니다."));
            // memberScrap table에 기록이 없으면 스크랩 설정 <-> table에 기록이 있다면 스크랩 해제
            Optional<MemberScrap> optionalMemberScrap = memberScrapRepository.findByMemberIdAndTopicId(memberId, topic.getId());
            optionalMemberScrap.ifPresentOrElse(
                    memberScrapRepository::delete, // 스크랩 기록이 있으면 삭제 (스크랩 해제)
                    () -> memberScrapRepository.save(new MemberScrap(member, topic)) // 스크랩 기록이 없으면 저장 (스크랩 설정)
            );
        } else if (dto.getType() == 2) { // gpt column 스크랩
            GptColumn gptColumn = gptColumnRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("GPT COLUMN ID가 존재하지 않습니다."));
            Optional<PostsScrap> firstByGptColumnAndMember = postsScrapRepository.findFirstByGptColumnAndMember(gptColumn, member);
            firstByGptColumnAndMember.ifPresentOrElse(
                    postsScrapRepository::delete,
                    () -> postsScrapRepository.save(new PostsScrap(gptColumn, member))
            );
        } else {
            return ResponseEntity.ok(ApiResponseWrapper.fail("type을 확인해주세요", dto.getType()));
        }

        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> recentSearch(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        List<RecentSearch> recentSearchList = recentSearchRepository.findByMemberOrderByLocalDateTimeDesc(member);
        List<RecentSearchResponseDto> responseDto = recentSearchList.stream()
                .map(recentSearch -> RecentSearchResponseDto.builder()
                        .id(recentSearch.getId())
                        .keyword(recentSearch.getKeyword())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> popularKeyword(){
        int rank = 1;
        List<PopularSearchDto> popularSearchDtoList = new ArrayList<>();
        List<WeekPopularSearch> weekPopularSearchList = weekPopularKeywordRepository.findAllByOrderByIdAsc();
        for (WeekPopularSearch weekPopularSearch : weekPopularSearchList) {
            popularSearchDtoList.add(new PopularSearchDto(rank, weekPopularSearch.getKeyword(), weekPopularSearch.getTrend()));
            rank += 1;
        }
        return ResponseEntity.ok(ApiResponseWrapper.success(new PopularSearchResponseDto(LocalDate.now(), popularSearchDtoList)));

    }

    public ResponseEntity<ApiResponseWrapper> deleteRecentKeyword(CustomUserDetails userDetails, DeleteRecentKeywordRequestDto dto){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        RecentSearch recentSearch = recentSearchRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("최근검색 ID가 존재하지 않습니다."));
        recentSearchRepository.delete(recentSearch);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> requestKeyword(CustomUserDetails userDetails, KeywordRequestDto dto){
        String requester = null;
        if (userDetails != null){
            Long memberId = userDetails.getMemberIdasLong();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
            requester = member.getEmail();
        }
        if (topicRequestRepository.existsByTerm(dto.getKeyword()) ){
            return ResponseEntity.ok(ApiResponseWrapper.success("이미 요청처리 된 단어입니다."));
        }
        TopicRequest topicRequest = TopicRequest.builder()
                .term(dto.getKeyword())
                .requester(requester)
                .requestedAt(LocalDateTime.now())
                .build();

        topicRequestRepository.save(topicRequest);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }


    public ResponseEntity<ApiResponseWrapper> topicInfo(CustomUserDetails userDetails, Long topicId){
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("토픽ID가 존재하지 않습니다."));
        // 제목, 요약, 원본

        boolean isScrapped = false;
        if (userDetails != null){
            Long memberId = userDetails.getMemberIdasLong();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
            // 유저가 스크랩했는지 확인
            isScrapped = memberScrapRepository.findByMemberIdAndTopicId(memberId, topicId).isPresent();
        }
        DetailTopicProcessDto topicInfoProcessDto = DetailTopicProcessDto.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .summary(topic.getSummary())
                .definition(topic.getDefinition())
                .isScrapped(isScrapped)
                .img_path(awsS3Service.combineWithBaseUrl(topic.getThumbnailImgPath()))
                .build();

        return ResponseEntity.ok(ApiResponseWrapper.success(new TopicInfoResponseDto(topicInfoProcessDto)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> usertypeList(){
        List<UserType> JobList = userTypeRepository.findAllByUseYNOrderByIdAsc("Y");
        List<UserTypeListProcessDto> processDto = JobList.stream()
                .map(userType -> UserTypeListProcessDto.builder()
                        .id(userType.getId())
                        .name(userType.getName())
                        .img_path(awsS3Service.combineWithBaseUrl(userType.getAvatarImgPath()))
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(new UserTypeListResponseDto(processDto)));
    }

    public ResponseEntity<ApiResponseWrapper> gptContent(Long categoryId, Long topicId, Long usertypeId){
        UserType userType = userTypeRepository.findById(usertypeId).orElseThrow(() -> new EntityNotFoundException("직업ID가 존재하지 않습니다."));
        Gpt findGpt = gptRepository.findByUserTypeIdAndCategoryAndTopicId(usertypeId, categoryId, topicId);
        GptContentProcessDto gptProcessDto = new GptContentProcessDto(userType.getName(), findGpt.getContent());
        return ResponseEntity.ok(ApiResponseWrapper.success(new GptContentResponseDto(gptProcessDto)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> nextTopic(Long categoryId, Long topicId){
        PageRequest request = PageRequest.of(0, 1);
        // 같은 category의 다음 topic 찾기
        Page<Topic> nextTopicPage = topicRepository.findNextTopicInSameCategory(categoryId, topicId, request);
        DetailNextTopicProcessDto nextTopicProcessDto = null;
        // 만약 다음 토픽이 존재하지 않는다면
        if(!nextTopicPage.isEmpty()){
            Topic nextTopic = nextTopicPage.getContent().get(0);
            nextTopicProcessDto= DetailNextTopicProcessDto.builder()
                    .id(nextTopic.getId())
                    .title(nextTopic.getTitle())
                    .img_path(awsS3Service.combineWithBaseUrl(nextTopic.getThumbnailImgPath())).build();
        }else {
            nextTopicProcessDto = new DetailNextTopicProcessDto(0L, "", "");
        }
        return ResponseEntity.ok(ApiResponseWrapper.success(new NextTopicResponseDto(nextTopicProcessDto)));
    }

    public ResponseEntity<ApiResponseWrapper> selectUsertype(CustomUserDetails userDetails, SelectUsertypeRequestDto dto){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        UserType userType = userTypeRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("직업ID가 존재하지 않습니다."));
        member.updateJob(userType);
        memberRepository.save(member);

        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> scrapList(CustomUserDetails userDetails, String type){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));

        switch (type){
            case "topic" -> {
                List<MemberScrap> scrapList = memberScrapRepository.findByMember(member);
                List<MyScrapTopicProcessDto> responseDto = scrapList.stream().map(
                                memberScrap -> MyScrapTopicProcessDto.builder()
                                        .categoryId(memberScrap.getTopic().getCategory().getId())
                                        .topicId(memberScrap.getTopic().getId())
                                        .title(memberScrap.getTopic().getTitle())
                                        .definition(memberScrap.getTopic().getDefinition())
                                        .build())
                        .collect(Collectors.toList());

                return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
            }
            case "column" -> {
                List<PostsScrap> scrapList = postsScrapRepository.findByMember(member);
                List<MyScrapColumnProcessDto> responseDto = scrapList.stream().map(
                                postsScrap -> MyScrapColumnProcessDto.builder()
                                        .columnId(postsScrap.getId())
                                        .title(postsScrap.getGptColumn().getTitle())
                                        .summary(postsScrap.getGptColumn().getSummary())
                                        .build())
                        .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
            }
            default -> throw new IllegalArgumentException("검색방법이 잘못되었습니다.");
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> list(Long categoryId){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow( () -> new EntityNotFoundException("카테고리ID가 존재하지 않습니다."));
        List<Topic> topicList = topicRepository.findByUseYNAndCategory("Y", findCategory);
        List<TopicListOnlyNameProcessDto> topicListDto = topicList.stream()
                .map(topic -> new TopicListOnlyNameProcessDto(topic.getId(), topic.getTitle())).collect(Collectors.toList());

        ListResponseDto responseDto = new ListResponseDto(findCategory.getId(), topicListDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }



    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> listAvatar(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));

        List<UserAvatar> avatarList = userAvatarRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<UserAvatarProcessDto> avatarProcessDtoList = avatarList.stream()
                .map(userAvatar -> UserAvatarProcessDto.builder()
                        .id(userAvatar.getId())
                        .imgUrl(awsS3Service.combineWithBaseUrl( userAvatar.getAvatar_img_path()))
                        .build())
                .collect(Collectors.toList());

        AvatarListResponseDto responseDto = new AvatarListResponseDto(avatarProcessDtoList);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> selectAvatar(CustomUserDetails userDetails, SelectAvatarRequestDto dto){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        UserAvatar userAvatar = userAvatarRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("아바타ID 존재하지 않습니다."));
        member.updateAvatar(userAvatar);
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }


    public ResponseEntity<ApiResponseWrapper> deleteAvatar(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        if (member.getUserAvatar() == null) {
            return ResponseEntity.ok(ApiResponseWrapper.success());
        }
        UserAvatar userAvatar = userAvatarRepository.findById(member.getUserAvatar().getId())
                .orElseThrow(() -> new EntityNotFoundException("아바타ID가 존재하지 않습니다."));
        member.removeUserAvatar();
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    // 배너 리스트
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> bannerList() {
        List<Banner> bannerList = bannerRepository.findTop3ByUseYNOrderByIdDesc("Y");
        // Banner 엔티티 리스트를 BannerListProcessDto 리스트로 변환
        List<BannerListProcessDto> bannerListProcessDtos = bannerList.stream()
                .map(banner -> new BannerListProcessDto(
                        banner.getId(),
                        banner.getTitle(),
                        banner.getSubTitle(),
                        banner.getBannerType(),
                        awsS3Service.combineWithBaseUrl(banner.getBannerImageUrl()),
                        banner.getLandingPageUrl()))
                .toList();
        return ResponseEntity.ok(ApiResponseWrapper.success(new BannerListResponseDto(bannerListProcessDtos)));
    }

    public ResponseEntity<ApiResponseWrapper> push(CustomUserDetails userDetails, PushYNRequestDto dto){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));


        member.updatePushYN(dto.isYn());
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> announcement(Long cursorId, int size){
        if (cursorId == null || cursorId == 0){
            cursorId = Long.MAX_VALUE;
        }

        List<Announcement> announcementList = announcementRepository.find7Announcement(cursorId, PageRequest.of(0, size));
        List<AnnouncementProcessDto> announcementProcessDto = announcementList.stream().map(
                announcement -> AnnouncementProcessDto.builder()
                        .id(announcement.getId())
                        .title(announcement.getTitle())
                        .content(announcement.getContent())
                        .time(announcement.getCreatedTime()).build()).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(new AnnouncementResponseDto(announcementProcessDto)));
    }

    public ResponseEntity<ApiResponseWrapper> updateFcmToken(CustomUserDetails userDetails, String fcmToken) {
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        member.updateFcmToken(fcmToken);
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> myCommentList(CustomUserDetails userDetails){
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        List<Comments> commentsList = commentsRepository.findByMember(member);
        List<MyCommentsListProcessDto> commentListProcessDto = commentsList.stream()
                .map(comments ->
                        new MyCommentsListProcessDto(
                            comments.getId(),
                            comments.getGptColumn().getTitle(),
                            comments.getGptColumn().getBackgroundUrl(),
                            comments.getContent(),
                            comments.getTotalLike()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(new MyCommentsListResponseDto(commentListProcessDto)));
    }

}