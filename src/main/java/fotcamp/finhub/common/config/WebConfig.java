package fotcamp.finhub.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${origin.admin}") String adminOrigin;
    @Value("${origin.main}") String mainOrigin;
    @Value("${origin.dev}") String devOrigin;
    @Value("${origin.localOne}") String localOne;
    @Value("${origin.localTwo}") String localTwo;
    @Value("${origin.localThree}") String localThree;
    @Value("${origin.serverSwagger}") String serverSwagger;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Admin 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/admin/**")
                .allowedOrigins(mainOrigin, adminOrigin, devOrigin, localOne, localTwo, localThree, serverSwagger)  // Admin 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);

        // Main 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/main/**")
                .allowedOrigins(adminOrigin, mainOrigin, devOrigin, localOne, localTwo, localThree, serverSwagger)  // Main 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);

        // Auth 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/auth/**")
                .allowedOrigins(adminOrigin, mainOrigin, devOrigin, localOne, localTwo, localThree, serverSwagger)  // Main 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

