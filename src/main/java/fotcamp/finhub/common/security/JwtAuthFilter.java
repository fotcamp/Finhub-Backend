package fotcamp.finhub.common.security;


import fotcamp.finhub.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{ // OncePerRequestFilter : 한 번 실행 보장

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;

    /**
     * JWT 토큰 검증 수행 필터
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        //JWT가 헤더에 있는 경우
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);
            //JWT 유효성 검증
            if(jwtUtil.validateToken(token)){
                Long userId = jwtUtil.getUserId(token);
                System.out.println(userId);
                // 유저와 토큰 일치 시 userDetails 생성
                UserDetails userDetails = customUserDetailService.loadUserByUsername(userId.toString());
                if(userDetails != null){
                    // UserDetails, Password, Role -> 접근권한 인증 Token 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // 현재 Request의 Security Context에 접근권한 설정
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            else {

            }
        }
        filterChain.doFilter(request,response);

    }
}
