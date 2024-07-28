package fotcamp.finhub.main.config;

import fotcamp.finhub.common.exception.FcmException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Getter
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

    @Bean
    public String getClientSecret() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(keyResource.getURI()));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("EC");
        PrivateKey privateKey =  kf.generatePrivate(spec);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setIssuer(teamId)
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .setExpiration(new Date(now + 3600000)) // 1 hour expiration
                .setIssuedAt(new Date(now))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

}
