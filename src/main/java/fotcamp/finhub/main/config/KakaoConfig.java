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
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String user_info_uri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String client_secretId;

}
