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
            @RequestParam(name = "date", required = false) String date){
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


}
