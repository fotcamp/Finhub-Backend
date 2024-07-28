package fotcamp.finhub.main.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppleJwtConfig {

    @Value("${spring.security.oauth2.client.provider.apple.token-uri}")
    private String accessTokenRequestUrl;
    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.apple.team-id}")
    private String teamId;
    @Value("${spring.security.oauth2.client.registration.apple.key-id}")
    private String keyId;
    @Value("${spring.security.oauth2.client.registration.apple.authorization-grant-type}")
    private String grant_type;
    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
    private String client_secretId;
    @Value("${spring.security.oauth2.client.registration.apple.client-name}")
    private String client_name;
    @Value("classpath:apple-secret.p8")
    private Resource keyResource;

    @Value("${custom-redirect-uri.apple.fedev}")
    private String redirect_uri_feDev;
    @Value("${custom-redirect-uri.apple.feprod}")
    private String redirect_uri_feProd;
    @Value("${custom-redirect-uri.apple.beprod}")
    private String redirect_uri_beProd;


}
