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
        String[] allowedOrigins = {
                prodAdminOrigin, prodMainOrigin, prodApiOrigin,
                devAdminOrigin, devMainOrigin, devApiOrigin,
                localOne, localTwo, localThree, localFour
        };

        applyCorsConfig(registry, "/api/v1/admin/**", allowedOrigins, "GET", "POST", "PUT", "DELETE", "PATCH");
        applyCorsConfig(registry, "/api/v1/main/**", allowedOrigins, "GET", "POST", "PUT", "DELETE", "PATCH");
        applyCorsConfig(registry, "/api/v1/member/**", allowedOrigins, "GET", "POST", "PUT", "DELETE", "PATCH");
        applyCorsConfig(registry, "/api/v1/auth/**", allowedOrigins, "GET", "POST", "PUT");
    }

    private void applyCorsConfig(CorsRegistry registry, String pathPattern, String[] allowedOrigins, String... allowedMethods) {
        registry.addMapping(pathPattern)
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

