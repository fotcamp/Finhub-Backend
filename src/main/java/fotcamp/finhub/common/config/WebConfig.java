package fotcamp.finhub.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${origin.admin}") String adminOrigin;
    @Value("${origin.main}") String mainOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Admin 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/admin/**")
                .allowedOrigins(mainOrigin, adminOrigin, "http://127.0.0.1:3000", "http://localhost:3000")  // Admin 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);

        // Main 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/main/**")
                .allowedOrigins(adminOrigin, mainOrigin, "http://127.0.0.1:3000", "http://localhost:3000")  // Main 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT")
                .allowedHeaders("*")
                .allowCredentials(true);

        // Auth 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/auth/**")
                .allowedOrigins(adminOrigin, mainOrigin, "http://127.0.0.1:3000", "http://localhost:3000")  // Main 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

