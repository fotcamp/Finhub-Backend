package fotcamp.finhub.common.security;


import fotcamp.finhub.common.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
            Long userId = jwtUtil.getUserId(token);
            System.out.println(userId);
            //
            UserDetails userDetails = customUserDetailService.loadUserByUsername(userId.toString());
            if(userDetails != null){
                //UserDetails, Password, Role -> 접근권한 인증 Token 생성
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 현재 Request의 Security Context에 접근권한 설정
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request,response);
    }
}