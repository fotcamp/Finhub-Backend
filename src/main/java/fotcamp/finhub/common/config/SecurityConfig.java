package fotcamp.finhub.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.security.*;
import fotcamp.finhub.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final ObjectMapper objectMapper;

    @Value("${api-header.key}") private String expectedHeaderKey;
    @Value("${api-header.value}") private String expectedHeaderValue;

   private static final String[] AUTH_WHITELIST = {
           "/api/v1/auth/**",
           "/api/v1/admin/login",
           "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
           "/api/v1/home/**"
   };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // CSRF, CORS
        http.csrf(AbstractHttpConfigurer::disable); // CSRF 토큰 사용 X -> disable 설정
        http.cors(Customizer.withDefaults()); // 다른 도메인의 웹 페이지에서 리소스에 접근할 수 있도록 허용

        // 세션 관리 상태 없음으로 구성, SpringSecurity가 세션 생성
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        // FormLogin, BasicHttp 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        // JwtExceptionFilter를 JwtAuthFilter를 앞에 추가
        http.addFilterBefore(new JwtAuthFilter(customUserDetailService, objectMapper, jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(objectMapper, expectedHeaderKey, expectedHeaderValue), JwtAuthFilter.class);
        http.exceptionHandling( (exceptionHandling) -> exceptionHandling
                .accessDeniedHandler(customAccessDeniedHandler));

        // 권한 규칙 생성
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH_WHITELIST).permitAll() // 로그인 없이 접근이 가능한 api
                .requestMatchers("/api/v1/admin/**").hasAnyRole("SUPER","BE","FE") //admin api는 관리자 계정만
                .requestMatchers("/api/v1/member/**").hasRole("USER")
                        .anyRequest().permitAll()
                /**
                 * 권한 규칙 요구사항
                 * SUPER 모든 API에 접근 허용
                 * BE는 읽기조회 + 수정 API까지 접근 허용
                 * FE는 읽기 조회 API만 */
        );

        return http.build();
    }
}
