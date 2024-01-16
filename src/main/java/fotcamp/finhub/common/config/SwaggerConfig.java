package fotcamp.finhub.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Finhub API 명세서",
                description = "Finhub API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi finhubApi() {
        String[] paths = {"/api/v1/**"};

        return GroupedOpenApi.builder()
                .group("Finhub API v1")
                .pathsToMatch(paths)
                .build();
    }
}
