package fotcamp.finhub.common.security;



import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.dto.ErrorResponseDto;
import fotcamp.finhub.common.exception.TokenNotValidateException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.LocalDateTime;

/** 인증되지 않은 사용자에 대해 처리하는 핸들러 */
@Component
@Slf4j(topic = "UNAUTHORIZATION_EXCEPTION_HANDLER")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;
    private HandlerExceptionResolver resolver;

    @Autowired
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper,
                                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.objectMapper = objectMapper;
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authenticationException) throws ServletException, IOException{

        Throwable cause = authenticationException.getCause();
        if (cause instanceof TokenNotValidateException){
            setResponse(request, response, authenticationException, cause.getMessage());
        } else {
            setResponse(request,response,authenticationException, "인증실패");
        }

        log.error("NOT Authenticated Request", authenticationException);
    }

    private void setResponse(HttpServletRequest request,HttpServletResponse response,AuthenticationException authenticationException,String msg) throws IOException {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(LocalDateTime.now(),401,"Authentication Error",msg,request.getRequestURI());
        String responseBody = objectMapper.writeValueAsString(errorResponseDto);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
        resolver.resolveException(request,response,null,authenticationException);
    }
}
