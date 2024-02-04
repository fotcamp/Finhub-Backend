package fotcamp.finhub.common.exception;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    // 값에 "", " " 등이 들어왔을 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper> handlingInvalidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        log.error("Invalid Exception from Exception Controller");
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(errorMessage));
    }

    // 값에 빈 공란 일때
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseWrapper> handlingInvalidException(HttpMessageNotReadableException e) {
        String errorMessage = "값 부분이 공란입니다";  // 예외 메시지를 새로 정의
        log.error("Invalid Exception from Exception Controller", e);
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(errorMessage));
    }
}
