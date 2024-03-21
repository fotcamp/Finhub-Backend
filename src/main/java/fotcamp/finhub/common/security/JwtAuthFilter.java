package fotcamp.finhub.common.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.api.ApiStatus;
import fotcamp.finhub.common.dto.response.ErrorMessageResponseDto;
import fotcamp.finhub.common.exception.ErrorMessage;
import fotcamp.finhub.common.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilter {

    private final CustomUserDetailService customUserDetailService;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 토큰 검증 필요없는 uri
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        if(requestURI.contains("/api/v1/auth/login")
                || requestURI.contains("/api/v1/auth/updateAccessToken")
                || requestURI.contains("/api/v1/auth/autoLogin")
                || requestURI.contains("/api/v1/admin/login")
                || requestURI.contains("/swagger-ui/")
                || requestURI.contains("/v3/api-docs")
                || requestURI.contains("/swagger-resources")
                || requestURI.equals("/swagger-ui.html"))

        {
            chain.doFilter(request,response);
            return;
        }

        // 토큰 추출
        String token = jwtUtil.resolveToken((HttpServletRequest) request);
        // http header에 key authorization value값이 null 혹은 빈 문자열 인 경우
        /**비로그인 유저라고 판단 */
        if (token == null || token.isEmpty()){ // null 또는 빈 문자열("")
            //setResponseInAuthFilter((HttpServletResponse) response, ErrorMessage.NULL_AUTHORIZATION_HEADER);
            chain.doFilter(request,response);
            return;
        }

        // 토큰 유효성 검증
        if(jwtUtil.validateToken(token)){
            Long memberId = jwtUtil.getUserId(token);
            String roleType = jwtUtil.getRoleType(token);
            CustomUserDetails userDetails;

            if(roleType.equals("ROLE_USER")){
                userDetails = customUserDetailService.loadUserByUsername(memberId.toString());
            }
            else {
                userDetails = customUserDetailService.loadAdminByRole(memberId.toString());
            }
            if(userDetails != null){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 현재 Request의 Security Context에 접근권한 설정
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request,response);
    }

    // 함수명 수정 ( exceptionFilter setResponse메소드가 오버라이딩 되어 제대로 동작 안함 )
    public void setResponseInAuthFilter(HttpServletResponse response, ErrorMessage errorMessage) throws RuntimeException, IOException {
        ErrorMessageResponseDto errMsg = new ErrorMessageResponseDto(ApiStatus.FAIL, errorMessage.getMsg(), errorMessage.toString());
        String responseMsg = objectMapper.writeValueAsString(errMsg);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getCode());
        response.getWriter().write(responseMsg);
    }
}