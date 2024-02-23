package fotcamp.finhub.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.dto.response.ErrorMessageResponseDto;
import fotcamp.finhub.common.exception.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** 인증은 되었지만 특정 리소스에 대한 권한이 없는 경우 호출되는 핸들러 */
@Component
@Slf4j(topic = "FORBIDDEN_EXCEPTION_HANDLER")
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws ServletException, IOException{
        log.error(accessDeniedException.getMessage());

        ErrorMessageResponseDto responseMsg = new ErrorMessageResponseDto(ErrorMessage.ACCESS_DENIED.getCode(), ErrorMessage.ACCESS_DENIED.toString(), ErrorMessage.ACCESS_DENIED.getMsg());
        String responseBody = objectMapper.writeValueAsString(responseMsg);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorMessage.ACCESS_DENIED.getCode());
        response.getWriter().write(responseBody);
    }
}