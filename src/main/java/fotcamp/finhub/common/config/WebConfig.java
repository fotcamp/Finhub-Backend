package fotcamp.finhub.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${origin.prod.admin}") String prodAdminOrigin;
    @Value("${origin.prod.main}") String prodMainOrigin;
    @Value("${origin.prod.api}") String prodApiOrigin;
    @Value("${origin.dev.admin}") String devAdminOrigin;
    @Value("${origin.dev.main}") String devMainOrigin;
    @Value("${origin.dev.api}") String devApiOrigin;
    @Value("${origin.local.localOne}") String localOne;
    @Value("${origin.local.localTwo}") String localTwo;
    @Value("${origin.local.localThree}") String localThree;
    @Value("${origin.local.localFour}") String localFour;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Admin 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/admin/**")
                .allowedOrigins(prodAdminOrigin, prodMainOrigin, prodApiOrigin, devAdminOrigin, devMainOrigin, devApiOrigin, localOne, localTwo, localThree, localFour)  // Admin 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);

        // Main 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/main/**")
                .allowedOrigins(prodAdminOrigin, prodMainOrigin, prodApiOrigin, devAdminOrigin, devMainOrigin, devApiOrigin, localOne, localTwo, localThree, localFour)  // Main 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);

        // Auth 애플리케이션을 위한 CORS 설정
        registry.addMapping("/api/v1/auth/**")
                .allowedOrigins(prodAdminOrigin, prodMainOrigin, prodApiOrigin, devAdminOrigin, devMainOrigin, devApiOrigin, localOne, localTwo, localThree, localFour)  // Main 프론트엔드 애플리케이션의 도메인
                .allowedMethods("GET", "POST", "PUT")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

