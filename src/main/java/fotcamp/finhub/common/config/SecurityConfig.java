package fotcamp.finhub.common.config;

import fotcamp.finhub.common.security.CustomAccessDeniedHandler;
import fotcamp.finhub.common.security.CustomAuthenticationEntryPoint;
import fotcamp.finhub.common.security.CustomUserDetailService;
import fotcamp.finhub.common.security.JwtAuthFilter;
import fotcamp.finhub.common.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** method 단위로 권한 제어 가능
 * ex)
 * @PreAuthroize("hasRole('ADMIN')") -> ADMIN만 해당 메소드 활용 가능
 * public responseDto testAPI(){}
 * */

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // CSRF, CORS
        http.csrf( (csrf) -> csrf.disable()); // CSRF 토큰 사용 X -> disable 설정
        http.cors(Customizer.withDefaults()); // 다른 도메인의 웹 페이지에서 리소스에 접근할 수 있도록 허용

        // 세션 관리 상태 없음으로 구성, SpringSecurity가 세션 생성
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        // FormLogin, BasicHttp 비활성화
        http.formLogin((form)->form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.exceptionHandling( (exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler));

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthFilter(customUserDetailService, jwtUtil), UsernamePasswordAuthenticationFilter.class);



        // 권한 규칙 생성
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH_WHITELIST).permitAll() // @PreAuthorization을 사용하기 때문에 모든 경로에 대한 인증처리는 여기서 안함
                .anyRequest().permitAll()
        );

        return http.build();
    }
}
