package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.dto.process.QuizTopicProcessDto;
import fotcamp.finhub.admin.repository.MemberQuizRepository;
import fotcamp.finhub.admin.repository.QuizRepository;
import fotcamp.finhub.admin.repository.TopicQuizRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberQuiz;
import fotcamp.finhub.common.domain.Quiz;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.response.quiz.*;
import fotcamp.finhub.main.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {
    private final QuizRepository quizRepository;
    private final TopicQuizRepository topicQuizRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final MemberRepository memberRepository;

    // 오늘 or 지난 날짜 퀴즈 가져오기 api 통합 service
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> findQuiz(CustomUserDetails userDetails, String date) {
        // 오늘의 퀴즈
        if (date == null) {
            LocalDate today = LocalDate.now();
            Quiz todayQuiz = quizRepository.findByTargetDate(today).orElseThrow(() -> new EntityNotFoundException("오늘의 퀴즈가 없습니다."));
            // 비로그인 일 경우
            if (userDetails == null) {
                return ResponseEntity.ok(ApiResponseWrapper.success(new QuizInfoResponseDto(new QuizInfoProcessDto(todayQuiz))));
            }

            // 유저가 이미 오늘 문제를 푼 경우, 예외 처리
            if (checkExistingMemberQuiz(userDetails, todayQuiz)) {
                return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("유저가 이미 푼 문제입니다."));
            }
            return ResponseEntity.ok(ApiResponseWrapper.success(new QuizInfoResponseDto(new QuizInfoProcessDto(todayQuiz))));
        }

        // 지난 날짜 퀴즈
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        Quiz prevQuiz = quizRepository.findByTargetDate(targetDate).orElseThrow(() -> new EntityNotFoundException("해당 날짜(" + targetDate.toString() + ")의 퀴즈가 없습니다."));

        // 유저가 이미 풀었던 문제일 경우, 예외 처리
        if (checkExistingMemberQuiz(userDetails, prevQuiz)) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("유저가 이미 푼 문제입니다."));
        }
        return ResponseEntity.ok(ApiResponseWrapper.success(new QuizInfoResponseDto(new QuizInfoProcessDto(prevQuiz))));
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

        List<QuizTopicProcessDto> quizTopicList = quiz.getTopicList().stream().map(QuizTopicProcessDto::new).toList();
        return ResponseEntity.ok(ApiResponseWrapper.success(new SolveQuizResponseDto(new SolveQuizProcessDto(memberQuiz.getAnswerYn(), quiz.getComment(), quizTopicList))));

    }

}
