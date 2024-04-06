package fotcamp.finhub.main.controller;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.response.quiz.SolveQuizRequestDto;
import fotcamp.finhub.main.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "main quiz", description = "main quiz api")
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


}
