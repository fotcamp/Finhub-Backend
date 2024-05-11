package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.dto.process.TopicIdTitleDto;
import fotcamp.finhub.admin.repository.CalendarEmoticonRepository;
import fotcamp.finhub.admin.repository.MemberQuizRepository;
import fotcamp.finhub.admin.repository.QuizRepository;
import fotcamp.finhub.admin.repository.TopicQuizRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.CalendarEmoticon;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberQuiz;
import fotcamp.finhub.common.domain.Quiz;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.utils.DateUtil;
import fotcamp.finhub.main.dto.response.quiz.*;
import fotcamp.finhub.main.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuizService {
    private final QuizRepository quizRepository;
    private final TopicQuizRepository topicQuizRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final MemberRepository memberRepository;
    private final CalendarEmoticonRepository calendarEmoticonRepository;
    private final AwsS3Service awsS3Service;

    // 오늘 or 지난 날짜 퀴즈 가져오기 api 통합 service
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> findQuiz(CustomUserDetails userDetails, String date) {
        // 오늘의 퀴즈
        if (date == null) {
            LocalDate today = LocalDate.now();
            Quiz todayQuiz = quizRepository.findByTargetDate(today).orElseThrow(() -> new EntityNotFoundException("오늘의 퀴즈가 없습니다."));
            // 비로그인 일 경우
            if (userDetails == null) {
                return ResponseEntity.ok(ApiResponseWrapper.success(new QuizInfoResponseDto(new NoSolvedQuizInfoProcessDto(todayQuiz))));
            }

            // 유저가 이미 오늘 문제를 푼 경우, 예외 처리
            if (checkExistingMemberQuiz(userDetails, todayQuiz)) {
                return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("유저가 이미 푼 문제입니다."));
            }
            return ResponseEntity.ok(ApiResponseWrapper.success(new QuizInfoResponseDto(new NoSolvedQuizInfoProcessDto(todayQuiz))));
        }

        // 지난 날짜 퀴즈
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        Quiz prevQuiz = quizRepository.findByTargetDate(targetDate).orElseThrow(() -> new EntityNotFoundException("해당 날짜(" + targetDate.toString() + ")의 퀴즈가 없습니다."));

        // 유저가 이미 풀었던 문제일 경우, 퀴즈의 전반적인 내용을 담아 return
        if (checkExistingMemberQuiz(userDetails, prevQuiz)) {
            Member member = memberRepository.findById(userDetails.getMemberIdasLong()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저."));
            MemberQuiz memberQuiz = memberQuizRepository.findByMemberAndQuiz(member, prevQuiz).get();
            List<TopicIdTitleDto> quizTopicList = prevQuiz.getTopicList().stream().map(TopicIdTitleDto::new).toList();
            return ResponseEntity.ok(ApiResponseWrapper.success(new SolveQuizResponseDto(new SolveQuizProcessDto(prevQuiz.getId(), memberQuiz.getAnswerYn(), prevQuiz.getComment(), quizTopicList))));
        }
        // 유저가 풀지 않은 문제일 경우, 문제만 return
        return ResponseEntity.ok(ApiResponseWrapper.success(new QuizInfoResponseDto(new NoSolvedQuizInfoProcessDto(prevQuiz))));
    }

    // 동일한 Member와 Quiz 조합이 있는지 확인하는 메서드
    private Boolean checkExistingMemberQuiz(CustomUserDetails userDetails, Quiz quiz) {
        Member member = memberRepository.findById(userDetails.getMemberIdasLong()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저"));
        Optional<MemberQuiz> checkExistingMemberQuiz = memberQuizRepository.findByMemberAndQuiz(member, quiz);
        return checkExistingMemberQuiz.isPresent();
    }

    // 오늘 or 지난 날짜 문제풀기 api
    public ResponseEntity<ApiResponseWrapper> solveQuiz(CustomUserDetails userDetails, SolveQuizRequestDto solveQuizRequestDto) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        Member member = memberRepository.findById(userDetails.getMemberIdasLong()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저"));
        Quiz quiz = quizRepository.findById(solveQuizRequestDto.id()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 퀴즈"));

        // 이미 존재하는 경우, 예외 처리나 적절한 로직을 수행
        if (checkExistingMemberQuiz(userDetails, quiz)) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("이미 푼 문제입니다."));
        }
        if (!solveQuizRequestDto.answer().equals("O") && !solveQuizRequestDto.answer().equals("X")) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("ANSWER 형식 오류"));
        }

        MemberQuiz memberQuiz = MemberQuiz.builder()
                .member(member)
                .quiz(quiz)
                .build();

        if (quiz.getAnswer().equals(solveQuizRequestDto.answer())) {
            memberQuiz.addAnswerYn("Y");
        } else {
            memberQuiz.addAnswerYn("N");
        }
        memberQuizRepository.save(memberQuiz);
        member.addMemberQuiz(memberQuiz);

        List<TopicIdTitleDto> quizTopicList = quiz.getTopicList().stream().map(TopicIdTitleDto::new).toList();
        return ResponseEntity.ok(ApiResponseWrapper.success(new SolveQuizResponseDto(new SolveQuizProcessDto(quiz.getId(), memberQuiz.getAnswerYn(), quiz.getComment(), quizTopicList))));
    }

    // 달력 퀴즈 데이터 가져오기 api
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> findCalendarQuiz(CustomUserDetails userDetails, String year, String month) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        Member member = memberRepository.findById(userDetails.getMemberIdasLong()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저."));
        String emoticonImgPath;
        if (member.getCalendarEmoticon() == null) {
            emoticonImgPath = null;
        } else {
            emoticonImgPath = awsS3Service.combineWithBaseUrl(member.getCalendarEmoticon().getEmoticon_img_path());
        }

        LocalDate startDate = DateUtil.convertToDate(Long.parseLong(year), Long.parseLong(month), 1L);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<MemberQuiz> solvedQuizzes = memberQuizRepository.findAllByMemberAndSolvedTimeBetween(
                member.getMemberId(), startDate, endDate);

        Map<LocalDate, String> quizCompletionStatus = new HashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            quizCompletionStatus.put(date, "N");
        }

        for (MemberQuiz solvedQuiz : solvedQuizzes) {
            LocalDate solvedDate = solvedQuiz.getQuiz().getTargetDate();
            quizCompletionStatus.put(solvedDate, "Y");
        }


        // quizCompletionStatus를 기반으로 resultDto 구성
        List<QuizDayStatusDto> quizDayStatusList = quizCompletionStatus.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 날짜별로 오름차순 정렬
                .map(entry -> new QuizDayStatusDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // 이모티콘 리스트 전달
        List<EmoticonDto> emoticonList = new ArrayList<>();
        List<CalendarEmoticon> allEmoticon = calendarEmoticonRepository.findAll();
        for (CalendarEmoticon calendarEmoticon : allEmoticon) {
            emoticonList.add(new EmoticonDto(calendarEmoticon.getId(), awsS3Service.combineWithBaseUrl(calendarEmoticon.getEmoticon_img_path())));
        }

        // ApiResponseWrapper에 결과 DTO를 담아 반환
        return ResponseEntity.ok(ApiResponseWrapper.success(new CalendarQuizResponseDto(emoticonImgPath, emoticonList, quizDayStatusList)));
    }

    // 놓친 퀴즈 리스트 가져오기 api
    public ResponseEntity<ApiResponseWrapper> missedQuizList(CustomUserDetails userDetails, LocalDate cursorDate, int limit) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        List<QuizInfoDto> missedQuizList = quizRepository.findMissedQuizInfoByMemberId(userDetails.getMemberIdasLong(), cursorDate, PageRequest.of(0, limit));
        return ResponseEntity.ok(ApiResponseWrapper.success(new SolvedQuizListResponseDto(missedQuizList)));
    }

    // 풀었던 퀴즈 리스트 가져오기 api
    public ResponseEntity<ApiResponseWrapper> solvedQuizList(CustomUserDetails userDetails, LocalDate cursorDate, int limit, String isCorrect) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }

        List<QuizInfoDto> solvedQuizList = memberQuizRepository.findSolvedQuizInfoByMemberId(userDetails.getMemberIdasLong(), cursorDate, isCorrect, PageRequest.of(0, limit));
        return ResponseEntity.ok(ApiResponseWrapper.success(new SolvedQuizListResponseDto(solvedQuizList)));
    }

    // 달력 이모티콘 저장하기
    public ResponseEntity<ApiResponseWrapper> emoticonSave(CustomUserDetails userDetails, EmoticonSaveRequestDto emoticonSaveRequestDto) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        Member member = memberRepository.findById(userDetails.getMemberIdasLong()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저."));
        CalendarEmoticon calendarEmoticon = calendarEmoticonRepository.findById(emoticonSaveRequestDto.id()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이모티콘."));
        member.updateCalendarEmoticon(calendarEmoticon);

        return ResponseEntity.ok(ApiResponseWrapper.success());
    }
}
