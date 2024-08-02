package fotcamp.finhub.main.config;


import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.security.Security;


@Getter
@Configuration
public class AppleJwtConfig {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Value("${spring.security.oauth2.client.provider.apple.token-uri}")
    private String identityTokenRequestUri;
    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
    private String client_secret;
    @Value("${spring.security.oauth2.client.registration.apple.team-id}")
    private String teamId;
    @Value("${spring.security.oauth2.client.registration.apple.key.id}")
    private String keyId;
    @Value("${spring.security.oauth2.client.registration.apple.key.private-key}")
    private String private_key;
    @Value("${spring.security.oauth2.client.registration.apple.authorization-grant-type}")
    private String grant_type;
    @Value("${spring.security.oauth2.client.registration.apple.client-name}")
    private String client_name;
    @Value("${spring.security.oauth2.client.provider.apple.public-key-url}")
    private String public_key_url;
    @Value("${custom-redirect-uri.apple.feprod}")
    private String redirect_uri_feProd;
    @Value("${custom-redirect-uri.apple.beprod}")
    private String redirect_uri_beProd;

}
