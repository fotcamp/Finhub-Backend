package fotcamp.finhub.main.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
public class KakaoConfig {

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String accessTokenRequestUrl;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grant_type;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String user_info_uri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String client_secretId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-name}")
    private String client_name;

    @Value("${custom-redirect-uri.kakao.felocal}")
    private String redirect_uri_feLocal;
    @Value("${custom-redirect-uri.kakao.feprod}")
    private String redirect_uri_feProd;
    @Value("${custom-redirect-uri.kakao.belocal}")
    private String redirect_uri_beLocal;
    @Value("${custom-redirect-uri.kakao.bedev}")
    private String redirect_uri_beDev;
    @Value("${custom-redirect-uri.kakao.beprod}")
    private String redirect_uri_beProd;

}
