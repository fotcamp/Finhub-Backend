package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.repository.*;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.*;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.main.dto.process.*;
import fotcamp.finhub.main.dto.request.ChangeNicknameRequestDto;
import fotcamp.finhub.main.dto.request.NewKeywordRequestDto;
import fotcamp.finhub.main.dto.request.SelectJobRequestDto;
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
    private final AwsS3Service awsS3Service;
    private final QuizRepository quizRepository;

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
            topicListProcessDtoList.add(new TopicListProcessDto(topic.getId(), topic.getTitle(), topic.getSummary(), isScrapped, firstCategory.getName()));
        }

        HomeResponseDto homeResponseDto = new HomeResponseDto(categoryListDtos, topicListProcessDtoList);
        return ResponseEntity.ok(ApiResponseWrapper.success(homeResponseDto));
    }
    
    public ResponseEntity<ApiResponseWrapper> changeNickname(CustomUserDetails userDetails , ChangeNicknameRequestDto dto){
        String newNickname = dto.getNewNickname();
        if (memberRepository.existsByNickname(newNickname)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("이미 존재하는 닉네임입니다."));
        }
        Long memberId = userDetails.getMemberIdasLong();
        Member existingMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 요청 데이터가 존재하지 않습니다."));
        existingMember.updateNickname(newNickname);
        memberRepository.save(existingMember);
        return ResponseEntity.ok(ApiResponseWrapper.success("변경 완료"));
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

    public ResponseEntity<ApiResponseWrapper> deleteRecentKeyword(CustomUserDetails userDetails, Long searchId){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        RecentSearch recentSearch = recentSearchRepository.findById(searchId).orElseThrow(() -> new EntityNotFoundException("최근검색 ID가 존재하지 않습니다."));
        recentSearchRepository.delete(recentSearch);
        return ResponseEntity.ok(ApiResponseWrapper.success("삭제 성공"));
    }

    public ResponseEntity<ApiResponseWrapper> deleteAllRecentKeyword(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        recentSearchRepository.deleteByMember_memberId(memberId);
        return ResponseEntity.ok(ApiResponseWrapper.success("삭제 성공"));
    }

    public ResponseEntity<ApiResponseWrapper> requestKeyword(CustomUserDetails userDetails, NewKeywordRequestDto dto){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        if (topicRequestRepository.existsByTerm(dto.getKeyword()) ){
            return ResponseEntity.ok(ApiResponseWrapper.success("이미 요청처리 된 단어입니다."));
        }
        topicRequestRepository.save(new TopicRequest(dto.getKeyword(), member.getName(), LocalDateTime.now()));
        return ResponseEntity.ok(ApiResponseWrapper.success("접수되었습니다."));
    }

    public ResponseEntity<ApiResponseWrapper> detailTopic(CustomUserDetails userDetails, Long categoryId, Long topicId){
        // 조회 토픽
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("토픽ID가 존재하지 않습니다."));
        // title, summary, definition
        DetailTopicProcessDto detailTopicProcessDto = new DetailTopicProcessDto(topic.getId(), topic.getTitle(), topic.getDefinition());

        // 로그인 유저 -> 스크랩 정보 + 직업리스트 + gpt content 설정 직업에 맞게 제공
        boolean isScrapped = false;
        Gpt findGpt = null;
        List<UserType> JobLists = userTypeRepository.findAllJobList();
        List<JobListProcessDto> jobListProcessDtos = JobLists.stream().
                map(UserType -> new JobListProcessDto(UserType.getId(), UserType.getName())).collect(Collectors.toList());
        if (userDetails != null){
            Long memberId = userDetails.getMemberIdasLong();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
            // 해당 멤버가 스크랩했는지 확인
            isScrapped = memberScrapRepository.findByMemberIdAndTopicId(memberId, topicId).isPresent();
            findGpt = gptRepository.findByUserTypeIdAndTopicId(member.getUserType().getId(), topicId);
        }
        // 비로그인 유저 -> 스크랩 false + 직업리스트 + 첫 번째 직업의 content 제공
        else{
            PageRequest limit = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));
            Page<UserType> firstUserTypePage = userTypeRepository.findFirstByOrderByNameAsc(limit);
            UserType firstUserType = firstUserTypePage.getContent().get(0);
            findGpt = gptRepository.findByUserTypeIdAndTopicId(firstUserType.getId(), topicId);
        }
        // 다음 토픽
        PageRequest request = PageRequest.of(0, 1);
        // categoryId로 topicList 중에서 topicId 다음걸 찾아야함
        Page<Topic> nextTopicPage = topicRepository.findNextTopicInSameCategory(categoryId, topicId, request);
        DetailNextTopicProcessDto nextTopicProcessDto = null;
        // 만약 다음 토픽이 존재하지 않는다면
        if(!nextTopicPage.isEmpty()){
            Topic nextTopic = nextTopicPage.getContent().get(0);
            nextTopicProcessDto = new DetailNextTopicProcessDto(nextTopic.getId(), nextTopic.getTitle());
        }else {
            nextTopicProcessDto = new DetailNextTopicProcessDto(0L, "");
        }


        DetailTopicResponseDto responseDto = new DetailTopicResponseDto(detailTopicProcessDto, nextTopicProcessDto, isScrapped, jobListProcessDtos, findGpt.getContent());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> menu(CustomUserDetails userDetails){

        String defaultAvatar = "";
        String defaultJob = "직업 없음";
        String defaultNickname = "닉네임을 입력하세요";

        if(userDetails == null){ // 비로그인
            return ResponseEntity.ok(ApiResponseWrapper.success("로그인이 필요해요."));
        }

        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        defaultAvatar = member.getUserAvatar() != null ? awsS3Service.combineWithBaseUrl(member.getUserAvatar().getAvatar_img_path()) : defaultAvatar;

        if (member.getUserType() != null){
            UserType userType = userTypeRepository.findById(member.getUserType().getId()).get();
            defaultJob = userType.getName();
        }

        MenuResponseDto responseDto = new MenuResponseDto(
                defaultAvatar, Optional.ofNullable(member.getNickname()).orElse(defaultNickname), defaultJob);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> profile(CustomUserDetails userDetails){

        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));

        if (member.getUserAvatar() != null){
            UserAvatar userAvatar = userAvatarRepository.findById(member.getUserAvatar().getId()).get();
            ProfileResponseDto responseDto = new ProfileResponseDto(
                    userAvatar.getId(),
                    awsS3Service.combineWithBaseUrl( userAvatar.getAvatar_img_path()),
                    member.getEmail());
            return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
        } else {
            ProfileResponseDto responseDto = new ProfileResponseDto(0L, "", member.getEmail());
            return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
        }
    }


    public ResponseEntity<ApiResponseWrapper> settingJob(CustomUserDetails userDetails){
        // 유저의 직업 먼저 확인 후, 없으면 '직업없음'으로 디폴트
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        String defaultJob = "직업 없음"; // 직업이 없으면 현재 직업은 '직업없음'

        if (member.getUserType().getName() != null) {
            defaultJob =member.getUserType().getName();
        }
        List<UserType> allJobList = userTypeRepository.findAllJobList();
        List<JobListProcessDto> jobListProcessDtos = allJobList.stream().map(UserType -> new JobListProcessDto(UserType.getId(), UserType.getName())).collect(Collectors.toList());
        SettingJobResponseDto responseDto = new SettingJobResponseDto(defaultJob, jobListProcessDtos);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> selectJob(CustomUserDetails userDetails, SelectJobRequestDto dto){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Optional<UserType> findUsertype = userTypeRepository.findByName(dto.getJob());
        if (findUsertype.isPresent()){
            member.updateJob(findUsertype.get());
            memberRepository.save(member);
            return ResponseEntity.ok(ApiResponseWrapper.success("직업 선택 성공"));
        }
        else{
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("요청한 직업명이 없습니다."));
        }
    }

    public ResponseEntity<ApiResponseWrapper> myScrap(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        List<MemberScrap> scrapList = memberScrapRepository.findByMember_memberId(memberId);
        List<MyScrapProcessDto> responseDto = scrapList.stream().map(MemberScrap -> new MyScrapProcessDto(MemberScrap.getTopic().getCategory().getId(), MemberScrap.getTopic().getId(), MemberScrap.getTopic().getTitle(), MemberScrap.getTopic().getDefinition())).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> listTab(){
        List<Category> allCategories = categoryRepository.findAllByOrderByIdAsc();
        List<CategoryListProcessDto> allCategoryListDto = allCategories.stream()
                .map(Category -> new CategoryListProcessDto(Category.getId(), Category.getName())).collect(Collectors.toList());

        Category firstCategory = categoryRepository.findFirstByOrderByIdAsc();
        List<Topic> topicList = topicRepository.findByCategory(firstCategory);
        List<FirstTopicListProcessDto> topicListDto = topicList.stream().map(Topic -> new FirstTopicListProcessDto(Topic.getId(), Topic.getTitle())).collect(Collectors.toList());

        ListTabResponseDto responseDto = new ListTabResponseDto(allCategoryListDto, topicListDto);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> listTabOthers(Long categoryId){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다."));
        List<Topic> topicList = topicRepository.findByCategory(findCategory);
        List<FirstTopicListProcessDto> responseDto = topicList.stream()
                .map(Topic -> new FirstTopicListProcessDto(Topic.getId(), Topic.getTitle())).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> listAvatar(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));

        String defaultAvatarUrl = "";
        if (member.getUserAvatar() != null) {
            UserAvatar userAvatar = userAvatarRepository.findById(member.getUserAvatar().getId()).get();
            defaultAvatarUrl = awsS3Service.combineWithBaseUrl(userAvatar.getAvatar_img_path());
        }
        List<UserAvatar> avatarList = userAvatarRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<UserAvatarProcessDto> avatarProcessDtoList = avatarList.stream()
                .map(UserAvatar -> new UserAvatarProcessDto(
                        UserAvatar.getId(),
                        awsS3Service.combineWithBaseUrl(UserAvatar.getAvatar_img_path())))
                .collect(Collectors.toList());

        AvatarListResponseDto responseDto = new AvatarListResponseDto(defaultAvatarUrl, avatarProcessDtoList);
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    public ResponseEntity<ApiResponseWrapper> selectAvatar(CustomUserDetails userDetails, Long avatarId){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        UserAvatar userAvatar = userAvatarRepository.findById(avatarId).orElseThrow(() -> new EntityNotFoundException("아바타ID 존재하지 않습니다."));
        member.updateAvatar(userAvatar);
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success("변경 완료"));
    }

    public ResponseEntity<ApiResponseWrapper> scrapOff(CustomUserDetails userDetails, Long topicId){

        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));

        MemberScrap memberScrap = memberScrapRepository.findByMemberIdAndTopicId(memberId, topicId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 스크랩 정보입니다."));
        memberScrapRepository.delete(memberScrap);
        member.removeScrap(memberScrap);
        memberRepository.save(member);

        List<MemberScrap> scrapList = memberScrapRepository.findByMember_memberId(memberId);
        List<MyScrapProcessDto> responseDto = scrapList.stream().map(MemberScrap -> new MyScrapProcessDto(MemberScrap.getTopic().getCategory().getId(), MemberScrap.getTopic().getId(), MemberScrap.getTopic().getTitle(), MemberScrap.getTopic().getDefinition())).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));

    }

    public ResponseEntity<ApiResponseWrapper> avatarOff(CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        if (member.getUserType() == null) {
            throw new EntityNotFoundException("아바타 설정 정보가 없습니다.");
        }
        UserAvatar userAvatar = userAvatarRepository.findById(member.getUserAvatar().getId())
                .orElseThrow(() -> new EntityNotFoundException("아바타ID가 존재하지 않습니다."));
        member.removeUserAvatar(userAvatar);
        memberRepository.save(member);

        return ResponseEntity.ok(ApiResponseWrapper.success("아바타 지우기 성공"));
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

    // 오늘의 퀴즈 보여주기
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> todayQuiz() {
        LocalDate today = LocalDate.now();
        Quiz quiz = quizRepository.findByTargetDate(today).orElseThrow(() -> new EntityNotFoundException("오늘의 퀴즈가 없습니다."));

        return ResponseEntity.ok(ApiResponseWrapper.success(new TodayQuizResponseDto(quiz.getQuestion())));
    }
}