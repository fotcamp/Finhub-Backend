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
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailService customUserDetailService;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
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

        String token = jwtUtil.resolveToken(request);
        if (token != null && !token.isEmpty() && jwtUtil.validateToken(token)) {
            Long memberId = jwtUtil.getUserId(token);
            String roleType = jwtUtil.getRoleType(token);
            CustomUserDetails userDetails = loadUserDetailsByRole(roleType, memberId);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    private CustomUserDetails loadUserDetailsByRole(String roleType, Long memberId) {
        if ("ROLE_USER".equals(roleType)) {
            return customUserDetailService.loadUserByUsername(memberId.toString());
        } else {
            return customUserDetailService.loadAdminByRole(memberId.toString());
        }
    }
}