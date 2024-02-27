package fotcamp.finhub.common.exception;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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
        log.error("값에 \"\", \" \"등이 들어왔을 때", e);
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(errorMessage));
    }

    // 값에 빈 공란 일때
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseWrapper> handlingInvalidException(HttpMessageNotReadableException e) {
        String errorMessage = "값 부분이 공란입니다";  // 예외 메시지를 새로 정의
        log.error("값 부분이 공란", e);
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(errorMessage));
    }

    // 파일 업로드 용량 초과시 발생
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ApiResponseWrapper> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("handleMaxUploadSizeExceededException", e);
        String errorMessage = "업로드 할 수 있는 파일의 최대 크기는 10MB 입니다.";
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiResponseWrapper> handledupEmailException(IllegalArgumentException e){
        log.error("중복되는 이메일",e);
        String msg = "중복되는 이메일입니다.";
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(msg));
    }
}


