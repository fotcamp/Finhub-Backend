package fotcamp.finhub.common.exception;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.DateTimeException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionController{

    // 값의 유효 범위를 벗어날 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper> handlingInvalidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
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

//    @ExceptionHandler(IllegalArgumentException.class)
//    protected ResponseEntity<ApiResponseWrapper> handledupEmailException(IllegalArgumentException e){
//        log.error("IllegalArgumentException", e);
//        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(e.getMessage()));
//    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponseWrapper> handleCorsException(Exception ex) {
        // 여기서 커스텀 응답을 생성하여 반환
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseWrapper.fail(
//                "CORS Policy Error 또는 접근권한 제한(터트리는 exception이 동일)"));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseWrapper.fail(ex.getMessage()));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiResponseWrapper> handleRestTemplateException(Exception e){
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("REST TEMPLATE ERROR"));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ApiResponseWrapper> handleDateTimeException(Exception e){
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("Invalid Date"));
    }

    // security 영역이 아닌 service계층에서 토큰 검사 도중 error가 터지면 여기서 catch함.
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseWrapper> handleJwtException(Exception e){
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseWrapper> handleArgumentException(Exception e){
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper> handEntityNotFoundException(Exception e){
        return ResponseEntity.badRequest().body(ApiResponseWrapper.fail(e.getMessage()));
    }

}


