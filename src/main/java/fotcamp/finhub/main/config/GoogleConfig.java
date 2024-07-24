package fotcamp.finhub.main.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GoogleConfig {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String client_secretId;
    @Value("${spring.security.oauth2.client.registration.google.client-name}")
    private String client_name;

    @Value("${custom-redirect-uri.google.felocal}")
    private String redirect_uri_feLocal;
    @Value("${custom-redirect-uri.google.fedev}")
    private String redirect_uri_feDev;
    @Value("${custom-redirect-uri.google.feprod}")
    private String redirect_uri_feProd;
    @Value("${custom-redirect-uri.google.belocal}")
    private String redirect_uri_beLocal;
    @Value("${custom-redirect-uri.google.bedev}")
    private String redirect_uri_beDev;
    @Value("${custom-redirect-uri.google.beprod}")
    private String redirect_uri_beProd;
}
