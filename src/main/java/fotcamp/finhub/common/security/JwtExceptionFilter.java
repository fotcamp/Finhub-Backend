package fotcamp.finhub.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.api.ApiStatus;
import fotcamp.finhub.common.dto.response.ErrorMessageResponseDto;
import fotcamp.finhub.common.exception.ErrorMessage;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter { // OncePerRequestFilter : 한 번 실행 보장

    private final ObjectMapper objectMapper;
    private final String expectedHeaderKey;
    private final String expectedHeaderValue;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        try {
            log.info("Header key: {}", expectedHeaderKey);
            log.info("Header value for key '{}': {}", expectedHeaderKey, request.getHeader(expectedHeaderKey));

            String requestURI = request.getRequestURI();
            if (requestURI.contains("/swagger-ui/")
                    || requestURI.contains("/v3/api-docs")
                    || requestURI.contains("/swagger-resources")
                    || requestURI.equals("/swagger-ui.html")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (request.getHeader(expectedHeaderKey) == null) {
                log.error(request.getHeader(expectedHeaderKey));
                setResponse(response, ErrorMessage.EMPTY_HEADER);
                return;
            } else if(Objects.equals(expectedHeaderValue, request.getHeader(expectedHeaderKey))){ //1차 보안 (사전에 약속된 key 하나를 default로 지정) : 헤더에 없으면 에러처리
                filterChain.doFilter(request, response);
            } else{
                log.error(request.getHeader(expectedHeaderKey));
                setResponse(response, ErrorMessage.NOT_CORRECT_HEADER);
                return;
            }
        } catch (JwtException ex) {
            String message = ex.getMessage();
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
        } catch (IllegalArgumentException e){
            setResponse(response, ErrorMessage.DUPLICATED_EMAIL);
        } catch (UsernameNotFoundException e){
            setResponse(response, ErrorMessage.NOT_FOUND);
        }
    }

    public void setResponse(HttpServletResponse response, ErrorMessage errorMessage) throws RuntimeException, IOException {
        ErrorMessageResponseDto errMsg = new ErrorMessageResponseDto(ApiStatus.FAIL, errorMessage.getMsg(), errorMessage.toString());
        String responseMsg = objectMapper.writeValueAsString(errMsg);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getCode());
        response.getWriter().write(responseMsg);
    }
}
