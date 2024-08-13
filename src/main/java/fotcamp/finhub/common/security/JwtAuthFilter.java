package fotcamp.finhub.common.security;


import com.fasterxml.jackson.databind.ObjectMapper;
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
            String uuid = jwtUtil.getUuid(token);
            String roleType = jwtUtil.getRoleType(token);
            CustomUserDetails userDetails = loadUserDetailsByRole(roleType, uuid);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    private CustomUserDetails loadUserDetailsByRole(String roleType, String uuid) {
        if ("ROLE_USER".equals(roleType)) {
            return customUserDetailService.loadUserByUsername(uuid);
        } else {
            return customUserDetailService.loadAdminByRole(uuid);
        }
    }
}