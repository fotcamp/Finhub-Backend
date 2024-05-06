package fotcamp.finhub.common.config;

import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class SwaggerConfig {

    @Value("${swagger.server-url}")
    private String serverUrl;


    @Bean
    public OpenAPI swaggerCustomUI() {
        return new OpenAPI()
                .info(getOpenAPIInfo())
                .components(getOpenAPIComponents())
                .addServersItem(new Server().url(serverUrl).description("Default Server URL"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    public Components getOpenAPIComponents() {
        return new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .description("Please enter 'Bearer' followed by a space and then your token"));
    }

    public Info getOpenAPIInfo() {
        return new Info()
                .title("Finhub API")
                .version("1.0")
                .description("Finhub API");
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("ADD_CUSTOM_HEADER")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    if (operation.getParameters() != null) {
                        operation.getParameters().addAll(this.getCustomerHeaders());
                    } else {
                        operation.setParameters(this.getCustomerHeaders());
                    }
                    return operation;
                })
                .build();
    }

    private List<Parameter> getCustomerHeaders() {
        List<Parameter> parameters = new ArrayList<>();
        Parameter customHeader = new HeaderParameter()
                .name("finhub")
                .description("Custom Header")
                .example("willbesuccess");

        parameters.add(customHeader);
        return parameters;
    }
}
