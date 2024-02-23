package fotcamp.finhub.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.dto.response.ErrorMessageResponseDto;
import fotcamp.finhub.common.exception.ErrorMessage;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter { // OncePerRequestFilter : 한 번 실행 보장

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            String message = ex.getMessage();
            System.out.println(message);
            if(ErrorMessage.UNKNOWN_ERROR.getMsg().equals(message)) {
                setResponse(response, ErrorMessage.UNKNOWN_ERROR);
            }
            //잘못된 타입의 토큰인 경우
            else if(ErrorMessage.WRONG_TYPE_TOKEN.getMsg().equals(message)) {
                setResponse(response, ErrorMessage.WRONG_TYPE_TOKEN);
            }
            //토큰 만료된 경우
            else if(ErrorMessage.EXPIRED_TOKEN.getMsg().equals(message)) {
                setResponse(response, ErrorMessage.EXPIRED_TOKEN);
            }
            //지원되지 않는 토큰인 경우
            else if(ErrorMessage.UNSUPPORTED_TOKEN.getMsg().equals(message)) {
                setResponse(response, ErrorMessage.UNSUPPORTED_TOKEN);
            }
            else {
                setResponse(response, ErrorMessage.ACCESS_DENIED);
            }
        }
    }

    public void setResponse(HttpServletResponse response, ErrorMessage errorMessage) throws RuntimeException, IOException {
        ErrorMessageResponseDto responseMsg = new ErrorMessageResponseDto(errorMessage.getCode(), errorMessage.toString(), errorMessage.getMsg());
        String responseBody = objectMapper.writeValueAsString(responseMsg);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getCode());
        response.getWriter().write(responseBody);
    }
}
