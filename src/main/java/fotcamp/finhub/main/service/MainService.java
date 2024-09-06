package fotcamp.finhub.main.service;


import fotcamp.finhub.admin.dto.request.CreateFcmMessageRequestDto;
import fotcamp.finhub.admin.repository.*;
import fotcamp.finhub.admin.service.FcmService;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.*;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.service.SlackWebhookService;
import fotcamp.finhub.main.dto.response.PushInfoResponseDto;
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
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {

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
    private final CommentsReportRepository commentsReportRepository;
    private final QuitReasonsRepository quitReasonsRepository;
    private final QuitMemberRepository quitMemberRepository;
    private final MemberNotificationRepository memberNotificationRepository;
    private final NotificationRepository notificationRepository;
    private final BlockRepository blockRepository;
    private final CommentsLikeRepository commentsLikeRepository;
    private final PostsLikeRepository postsLikeRepository;
    private final FeedbackRepository feedbackRepository;
    private final AgreementRepository agreementRepository;

    private final FcmService fcmService;
    private final SlackWebhookService slackService;

    private static final int MAX_RECENT_SEARCHES = 10;

    // 전체 카테고리 리스트
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> categoryList() {
        List<Category> allCategories = categoryRepository.findAllByUseYNAndPositionIsNotNullOrderByPositionAsc("Y");
        List<CategoryListProcessDto> categoryList = allCategories.stream()
                .map(category -> new CategoryListProcessDto(category.getId(), category.getName())).collect(Collectors.toList());
        CategoryListResponseDto responseDto = new CategoryListResponseDto(categoryList);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    // 토픽 리스트
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> topicList(CustomUserDetails userDetails, Long categoryId, Long cursorId, int size) {
        // 요청받은 카테고리의 토픽 7개
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));
        List<Topic> topicTop7 = topicRepository.findByCategoryAndIdGreaterThan(findCategory, cursorId, PageRequest.of(0, size));

        // 로그인 유무에 따라서 스크랩 정보 추가
        List<TopicListProcessDto> topicListProcessDtoList = new ArrayList<>();

        for (Topic topic : topicTop7) {
            boolean isScrapped = false;
            if (userDetails != null) {
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
                    .img_path(awsS3Service.combineWithCloudFrontBaseUrl(topic.getThumbnailImgPath())).build();
            topicListProcessDtoList.add(processDto);
        }
        return ResponseEntity.ok(ApiResponseWrapper.success(new TopicListResponseDto(topicListProcessDtoList)));
    }

    public ResponseEntity<ApiResponseWrapper> changeNickname(CustomUserDetails userDetails, ChangeNicknameRequestDto dto) {
        String newNickname = dto.getNickname();
        if (memberRepository.existsByNickname(newNickname)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("이미 존재하는 닉네임입니다."));
        }
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        member.updateNickname(newNickname);
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> membershipResign(CustomUserDetails userDetails, QuitRequestDto dto) {
        Long memberId = userDetails.getMemberIdasLong();
        Member existingMember = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("해당 멤버는 존재하지 않습니다."));
        QuitMember quitMember = QuitMember.builder()
                .age(userDetails.getUsername())
                .gender(userDetails.getUsername())
                .reasonId(dto.id())
                .reason(dto.reason())
                .build();
        quitMemberRepository.save(quitMember);
        // 멤버가 작성한 모든 댓글 조회
        List<Comments> commentAll = commentsRepository.findByMember(existingMember);
        // 각 댓글에 타인이 눌렀던 좋아요 기록 삭제
        for (Comments comment : commentAll) {
            List<CommentsLike> commentsLikeAll = commentsLikeRepository.findByComment(comment);
            commentsLikeRepository.deleteAll(commentsLikeAll);
        }

        // 해당 멤버가 누른 좋아요 기록 삭제
        List<CommentsLike> userCommentsLikes = commentsLikeRepository.findByMember(existingMember);
        commentsLikeRepository.deleteAll(userCommentsLikes);

        // 댓글 삭제
        commentsRepository.deleteAll(commentAll);

        // 컬럼 스크랩 삭제
        List<PostsScrap> postScrapAll = postsScrapRepository.findByMember(existingMember);
        postsScrapRepository.deleteAll(postScrapAll);

        // 컬럼 좋아요 삭제
        List<PostsLike> postsLikeList = postsLikeRepository.findByMember(existingMember);
        postsLikeRepository.deleteAll(postsLikeList);

        // 차단 목록 삭제
        List<Block> blockListAll = blockRepository.findByMember(existingMember);
        List<Block> blockMemberListAll = blockRepository.findByBlockMember(existingMember);
        blockRepository.deleteAll(blockListAll);
        blockRepository.deleteAll(blockMemberListAll);

        // 댓글 신고 목록 삭제
        List<CommentsReport> reporterList = commentsReportRepository.findByReporterMember(existingMember);
        List<CommentsReport> reportedList = commentsReportRepository.findByReportedMember(existingMember);
        commentsReportRepository.deleteAll(reporterList);
        commentsReportRepository.deleteAll(reportedList);

        // 동의 항목 목록 삭제
        MemberAgreement memberAgreement = agreementRepository.findByMember(existingMember);
        agreementRepository.delete(memberAgreement);

        memberRepository.delete(existingMember);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public SearchTopicResponseDto searchTopic(CustomUserDetails userDetails, String method, String keyword, Pageable pageable) {
        Page<Topic> pageResult = null;
        switch (method) {
            case "title" -> {
                pageResult = topicRepository.findByUseYNAndTitleContaining("Y", keyword, pageable);
            }
            case "summary" -> {
                pageResult = topicRepository.findByUseYNAndSummaryContaining("Y", keyword, pageable);
            }
            case "both" -> {
                pageResult = topicRepository.findByUseYNAndTitleContainingOrSummaryContaining("Y", keyword, keyword, pageable);
            }
            default -> throw new IllegalArgumentException("검색방법이 잘못되었습니다.");
        }
        // 인기검색어 카운트 기능 ( 해당 키워드로 첫 검색 상황 )
        if (pageable.getPageNumber() == 0) {
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

    public void handleRecentSearch(Long memberId, String keyword) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        RecentSearch recentSearch = recentSearchRepository.findByMemberAndKeyword(member, keyword)
                .orElse(new RecentSearch(member, keyword, LocalDateTime.now()));
        recentSearch.updateRecord(LocalDateTime.now());
        recentSearchRepository.save(recentSearch);

        List<RecentSearch> recentSearchList = recentSearchRepository.findByMemberOrderByLocalDateTimeDesc(member);
        if (recentSearchList.size() > MAX_RECENT_SEARCHES) { // 최대 10개까지만 최근검색키워드 저장
            recentSearchRepository.delete(recentSearchList.get(recentSearchList.size() - 1));
        }
    }

    public void incrementPopularKeyword(String keyword) {
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

    public ResponseEntity<ApiResponseWrapper> scrap(CustomUserDetails userDetails, ScrapRequestDto dto) {
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

    public ResponseEntity<ApiResponseWrapper> recentSearch(CustomUserDetails userDetails) {
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

    public ResponseEntity<ApiResponseWrapper> popularKeyword() {
        int rank = 1;
        List<PopularSearchDto> popularSearchDtoList = new ArrayList<>();
        List<WeekPopularSearch> weekPopularSearchList = weekPopularKeywordRepository.findWeekPopularSearchWithMaxAnalysisDate();
        for (WeekPopularSearch weekPopularSearch : weekPopularSearchList) {
            popularSearchDtoList.add(new PopularSearchDto(rank, weekPopularSearch.getKeyword(), weekPopularSearch.getTrend()));
            rank += 1;
        }
        return ResponseEntity.ok(ApiResponseWrapper.success(new PopularSearchResponseDto(LocalDate.now(), popularSearchDtoList)));

    }

    public ResponseEntity<ApiResponseWrapper> deleteRecentKeyword(CustomUserDetails userDetails, DeleteRecentKeywordRequestDto dto) {
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        RecentSearch recentSearch = recentSearchRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("최근검색 ID가 존재하지 않습니다."));
        recentSearchRepository.delete(recentSearch);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> requestKeyword(CustomUserDetails userDetails, KeywordRequestDto dto) {
        String requester = null;
        if (userDetails != null) {
            Long memberId = userDetails.getMemberIdasLong();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
            requester = member.getEmail();
        }
        if (topicRequestRepository.existsByTerm(dto.getKeyword())) {
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


    public ResponseEntity<ApiResponseWrapper> topicInfo(CustomUserDetails userDetails, Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("토픽ID가 존재하지 않습니다."));
        // 제목, 요약, 원본

        boolean isScrapped = false;
        if (userDetails != null) {
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
                .img_path(awsS3Service.combineWithCloudFrontBaseUrl(topic.getThumbnailImgPath()))
                .build();

        return ResponseEntity.ok(ApiResponseWrapper.success(new TopicInfoResponseDto(topicInfoProcessDto)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> usertypeList() {
        List<UserType> JobList = userTypeRepository.findAllByUseYNOrderByIdAsc("Y");
        List<UserTypeListProcessDto> processDto = JobList.stream()
                .map(userType -> UserTypeListProcessDto.builder()
                        .id(userType.getId())
                        .name(userType.getName())
                        .img_path(awsS3Service.combineWithCloudFrontBaseUrl(userType.getAvatarImgPath()))
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(new UserTypeListResponseDto(processDto)));
    }

    public ResponseEntity<ApiResponseWrapper> gptContent(Long categoryId, Long topicId, Long usertypeId) {
        UserType userType = userTypeRepository.findById(usertypeId).orElseThrow(() -> new EntityNotFoundException("직업ID가 존재하지 않습니다."));
        Gpt findGpt = gptRepository.findByUserTypeIdAndCategoryAndTopicId(usertypeId, categoryId, topicId);
        GptContentProcessDto gptProcessDto = new GptContentProcessDto(userType.getName(), findGpt.getContent());
        return ResponseEntity.ok(ApiResponseWrapper.success(new GptContentResponseDto(gptProcessDto)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> nextTopic(Long categoryId, Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("직업ID가 존재하지 않습니다."));
        PageRequest request = PageRequest.of(0, 1);
        // 같은 category에 속하는 토픽들 중 해당 position 다음 순번의 토픽 찾기
        Page<Topic> nextTopicPage = topicRepository.findNextTopicInSameCategory(categoryId, topic.getPosition(), request);
        DetailNextTopicProcessDto nextTopicProcessDto = null;

        if (!nextTopicPage.isEmpty()) {
            Topic nextTopic = nextTopicPage.getContent().get(0);
            nextTopicProcessDto = DetailNextTopicProcessDto.builder()
                    .id(nextTopic.getId())
                    .title(nextTopic.getTitle())
                    .img_path(awsS3Service.combineWithCloudFrontBaseUrl(nextTopic.getThumbnailImgPath())).build();
        } else { //만약 다음 토픽이 존재하지 않는다면
            nextTopicProcessDto = new DetailNextTopicProcessDto(0L, "", "");
        }
        return ResponseEntity.ok(ApiResponseWrapper.success(new NextTopicResponseDto(nextTopicProcessDto)));
    }

    public ResponseEntity<ApiResponseWrapper> selectUsertype(CustomUserDetails userDetails, SelectUsertypeRequestDto dto) {
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        long usertype_id = dto.getId();
        if (usertype_id == 0) {
            member.updateJob(null);
        } else {
            UserType userType = userTypeRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("직업ID가 존재하지 않습니다."));
            member.updateJob(userType);
        }
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> scrapList(CustomUserDetails userDetails, String type) {
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));

        switch (type) {
            case "topic" -> {
                List<MemberScrap> scrapList = memberScrapRepository.findByMember(member);
                List<MyScrapTopicProcessDto> responseDto = scrapList.stream().map(
                                memberScrap -> MyScrapTopicProcessDto.builder()
                                        .categoryId(memberScrap.getTopic().getCategory().getId())
                                        .topicId(memberScrap.getTopic().getId())
                                        .title(memberScrap.getTopic().getTitle())
                                        .definition(memberScrap.getTopic().getDefinition())
                                        .imgUrl(memberScrap.getTopic().getThumbnailImgPath())
                                        .build())
                        .collect(Collectors.toList());

                return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
            }
            case "column" -> {
                List<PostsScrap> scrapList = postsScrapRepository.findByMember(member);
                List<MyScrapColumnProcessDto> responseDto = scrapList.stream().map(
                                postsScrap -> MyScrapColumnProcessDto.builder()
                                        .columnId(postsScrap.getGptColumn().getId())
                                        .title(postsScrap.getGptColumn().getTitle())
                                        .summary(postsScrap.getGptColumn().getSummary())
                                        .imgUrl(postsScrap.getGptColumn().getBackgroundUrl())
                                        .build())
                        .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
            }
            default -> throw new IllegalArgumentException("검색방법이 잘못되었습니다.");
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> list(Long categoryId) {
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("카테고리ID가 존재하지 않습니다."));
        List<Topic> topicList = topicRepository.findByUseYNAndCategoryAndPositionIsNotNullOrderByPositionAsc("Y", findCategory);
        List<TopicListOnlyNameProcessDto> topicListDto = topicList.stream()
                .map(topic -> new TopicListOnlyNameProcessDto(topic.getId(), topic.getTitle())).collect(Collectors.toList());

        ListResponseDto responseDto = new ListResponseDto(findCategory.getId(), topicListDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }


    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> listAvatar(CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));

        List<UserAvatar> avatarList = userAvatarRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<UserAvatarProcessDto> avatarProcessDtoList = avatarList.stream()
                .map(userAvatar -> UserAvatarProcessDto.builder()
                        .id(userAvatar.getId())
                        .imgUrl(awsS3Service.combineWithCloudFrontBaseUrl(userAvatar.getAvatar_img_path()))
                        .build())
                .collect(Collectors.toList());

        AvatarListResponseDto responseDto = new AvatarListResponseDto(avatarProcessDtoList);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> selectAvatar(CustomUserDetails userDetails, SelectAvatarRequestDto dto) {
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        UserAvatar userAvatar = userAvatarRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("아바타ID 존재하지 않습니다."));
        member.updateAvatar(userAvatar);
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }


    public ResponseEntity<ApiResponseWrapper> deleteAvatar(CustomUserDetails userDetails) {
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
                        awsS3Service.combineWithCloudFrontBaseUrl(banner.getBannerImageUrl()),
                        banner.getLandingPageUrl()))
                .toList();
        return ResponseEntity.ok(ApiResponseWrapper.success(new BannerListResponseDto(bannerListProcessDtos)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> announcement(Long cursorId, int size) {
        if (cursorId == null || cursorId == 0) {
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

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> myCommentList(CustomUserDetails userDetails) {
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        List<Comments> commentsList = commentsRepository.findByMemberAndUseYn(member, "Y");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<MyCommentsListProcessDto> commentListProcessDto =
                commentsList.stream().map(comments ->
                        new MyCommentsListProcessDto(
                                comments.getId(),
                                comments.getGptColumn().getId(),
                                comments.getGptColumn().getTitle(),
                                comments.getContent(),
                                comments.getTotalLike(),
                                comments.getCreatedTime().format(formatter)
                        )
                ).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(new MyCommentsListResponseDto(commentListProcessDto)));
    }

    // 회원 탈퇴 이유 가져오기
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> quitReasons() {
        List<QuitReasons> quitReasons = quitReasonsRepository.findByUseYn("Y");
        List<QuitReasonsProcessDto> quitReasonsProcessDtos = quitReasons.stream().map(
                quitReason -> new QuitReasonsProcessDto(quitReason.getId(), quitReason.getReason())
        ).toList();

        return ResponseEntity.ok(ApiResponseWrapper.success(new QuitReasonsResponseDto(quitReasonsProcessDtos)));

    }

    public ResponseEntity<ApiResponseWrapper> alarmList(CustomUserDetails userDetails, Long cursorId, int size) {
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }
        Pageable pageable = PageRequest.of(0, size);

        Slice<MemberNotification> notifications = memberNotificationRepository.findNotificationsForMember(member, cursorId, pageable);
        List<AlarmDetailProcessDto> notificationResponseDto = notifications.getContent().stream().map(
                        memberNotification -> AlarmDetailProcessDto.builder()
                                .id(memberNotification.getNotification().getId())
                                .title(memberNotification.getNotification().getTitle())
                                .message(memberNotification.getNotification().getMessage())
                                .url(memberNotification.getNotification().getUrl())
                                .sentAt(memberNotification.getSentAt())
                                .receivedAt(memberNotification.getReceivedAt())
                                .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(new AlarmListResponseDto(notificationResponseDto)));
    }

    public ResponseEntity<ApiResponseWrapper> alarmDetail(CustomUserDetails userDetails, AlarmDetailRequestDto dto) {
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Notification notification = notificationRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("알람이 존재하지 않습니다."));
        MemberNotification memberNotification = memberNotificationRepository.findByMemberAndNotification(member, notification).orElseThrow(() -> new EntityNotFoundException("기록이 존재하지 않습니다."));

        String url = notification.getUrl();
        memberNotification.updateMemberNotification();
        return ResponseEntity.ok(ApiResponseWrapper.success(new AlarmDetailResponseDto(url)));
    }

    public ResponseEntity<ApiResponseWrapper> feedback(String userAgent, String appVersion, FeedbackRequestDto dto, CustomUserDetails userDetails) throws IOException, MessagingException {
        // 이미지 파일 리스트 s3에 업로드 진행 후, url들 db에 저장
        List<String> uploadedFileUrls = awsS3Service.uploadFiles(dto.getFiles(), "feedback");

        Feedback newVoc = Feedback.builder()
                .userAgent(userAgent)
                .appVersion(appVersion)
                .email(dto.getEmail())
                .fileUrl1(getVocFileUrl(uploadedFileUrls, 0))
                .fileUrl2(getVocFileUrl(uploadedFileUrls, 1))
                .fileUrl3(getVocFileUrl(uploadedFileUrls, 2))
                .fileUrl4(getVocFileUrl(uploadedFileUrls, 3))
                .feedback(dto.getText())
                .build();
        feedbackRepository.save(newVoc);
        if (userDetails != null) {
            // 액세스토큰 기준 멤버 지명해서 푸시 날려야함 ( 비로그인 유저 푸시 X)
            Long memberId = userDetails.getMemberIdasLong();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
            if (!member.getMemberAgreement().isPushYn()) {
                return ResponseEntity.ok(ApiResponseWrapper.success());
            }
            CreateFcmMessageRequestDto pushProcessDto = CreateFcmMessageRequestDto.builder()
                    .type(0L)
                    .target(Collections.singletonList(member.getEmail()))
                    .title("VOC 접수 완료")
                    .content("작성하신 건의사항이 접수되었습니다.")
                    .action(FcmMessageProcessDto.Action.
                            builder()
                            .date(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .id(0L)
                            .build())
                    .build();
            fcmService.sendFcmNotifications(pushProcessDto);
        }
        slackService.sendMsg(dto.getEmail(), dto.getText(), newVoc.getId());
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    private String getVocFileUrl(List<String> urls, int index) {
        // 리스트의 크기보다 인덱스가 작으면 해당 인덱스의 URL 반환, 그렇지 않으면 null 반환
        return (index < urls.size()) ? urls.get(index) : null;
    }

    public ResponseEntity<ApiResponseWrapper> deleteFcmToken(CustomUserDetails userDetails){
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        member.removeFcmToken();
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> postPush(CustomUserDetails userDetails, PushYNRequestDto dto) {
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        member.getMemberAgreement().updatePushYnState(dto.isPushYn());
        agreementRepository.save(member.getMemberAgreement());
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> getPush(CustomUserDetails userDetails){
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        return ResponseEntity.ok(ApiResponseWrapper.success(new PushInfoResponseDto(member.getMemberAgreement().isPushYn())));
    }

}