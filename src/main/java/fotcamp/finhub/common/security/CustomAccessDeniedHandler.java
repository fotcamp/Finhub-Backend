package fotcamp.finhub.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.dto.ErrorResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.LocalDateTime;

/** 인증은 되었지만 특정 리소스에 대한 권한이 없는 경우 호출되는 핸들러 */
@Component
@Slf4j(topic = "FORBIDDEN_EXCEPTION_HANDLER")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    public CustomAccessDeniedHandler(ObjectMapper objectMapper, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.objectMapper = objectMapper;
        this.resolver = resolver;
    }

    private final ObjectMapper objectMapper;
    private final HandlerExceptionResolver resolver;


    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws ServletException, IOException{
        log.error("No Authorities", accessDeniedException);
        ErrorResponseDto errorResponseDto =
                new ErrorResponseDto(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), "FORBIDDEN", "접근 권한이 없습니다.", request.getRequestURI());
                //new ErrorResponseDto(HttpStatus.FORBIDDEN,"권한이 없습니다.", LocalDateTime.now());
       String responseBody = objectMapper.writeValueAsString(errorResponseDto);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
        resolver.resolveException(request,response,null,accessDeniedException);
    }
}
