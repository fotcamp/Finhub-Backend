package fotcamp.finhub.common.security;


import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.domain.RoleType;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


import java.io.IOException;
import java.util.jar.JarException;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilter {

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰 추출
        String token = jwtUtil.resolveToken((HttpServletRequest) request);
        // 토큰 유효성 검증
        if(token != null && jwtUtil.validateToken(token)){
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
}