package fotcamp.finhub.main.controller;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.response.quiz.EmoticonSaveRequestDto;
import fotcamp.finhub.main.dto.response.quiz.SolveQuizRequestDto;
import fotcamp.finhub.main.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Tag(name = "D main quiz", description = "main quiz api")
@RestController
@RequestMapping("/api/v1/main/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping
    @Operation(summary = "퀴즈 가져오기", description = "오늘의 퀴즈, 놓친 퀴즈 가져오기")
    public ResponseEntity<ApiResponseWrapper> findQuiz(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "date", required = false) String date
    ) {
        return quizService.findQuiz(userDetails, date);
    }

    @PostMapping
    @Operation(summary = "퀴즈 풀기", description = "오늘의 퀴즈, 놓친 퀴즈 풀기")
    public ResponseEntity<ApiResponseWrapper> solveQuiz(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SolveQuizRequestDto solveQuizRequestDto
    ) {
        return quizService.solveQuiz(userDetails, solveQuizRequestDto);
    }

    @GetMapping("/{year}/{month}")
    @Operation(summary = "퀴즈 달력 가져오기", description = "퀴즈 달력 가져오기")
    public ResponseEntity<ApiResponseWrapper> findCalendarQuiz(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "year") String year,
            @PathVariable(name = "month") String month
    ) {
        if (!(year.length() == 4) || !(1 <= month.length() && month.length() <= 2) || Long.parseLong(month) < 1 || Long.parseLong(month) > 12) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("날짜 형식을 확인해주세요"));
        }

        return quizService.findCalendarQuiz(userDetails, year,month);
    }

    @GetMapping("/missed")
    @Operation(summary = "놓친 퀴즈 가져오기", description = "놓친 퀴즈 리스트 가져오기")
    public ResponseEntity<ApiResponseWrapper> missedQuizList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "date", required = false) Optional<String> date,
            @RequestParam(value = "limit", defaultValue = "3") int limit
    ) {
        // dateStringOptional이 비어 있으면 오늘 날짜를 문자열로 설정
        String dateString = date.orElseGet(() -> LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        LocalDate cursorDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return quizService.missedQuizList(userDetails, cursorDate, limit);
    }

    @GetMapping("/solved/{isCorrect}")
    @Operation(summary = "풀었던 퀴즈 가져오기", description = "풀었던 퀴즈 리스트 가져오기")
    public ResponseEntity<ApiResponseWrapper> solvedQuizList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "isCorrect") String isCorrect,
            @RequestParam(value = "date", required = false) Optional<String> date,
            @RequestParam(value = "limit", defaultValue = "3") int limit
    ) {
        // dateStringOptional이 비어 있으면 오늘 날짜를 문자열로 설정
        String dateString = date.orElseGet(() -> LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        LocalDate cursorDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return quizService.solvedQuizList(userDetails, cursorDate, limit, isCorrect);
    }

    @PostMapping("/calendar-emoticon")
    @Operation(summary = "캘린더 이모티콘 저장하기", description = "캘린더 이모티콘 저장하기")
    public ResponseEntity<ApiResponseWrapper> solvedQuizList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody EmoticonSaveRequestDto emoticonSaveRequestDto
    ) {
        return quizService.emoticonSave(userDetails, emoticonSaveRequestDto);
    }
}
