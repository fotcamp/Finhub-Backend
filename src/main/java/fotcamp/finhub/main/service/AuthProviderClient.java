package fotcamp.finhub.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.main.config.AppleJwtConfig;
import fotcamp.finhub.main.config.GoogleConfig;
import fotcamp.finhub.main.config.KakaoConfig;
import fotcamp.finhub.main.config.OAuth2Util;
import fotcamp.finhub.main.dto.process.login.KakaoUserInfoProcessDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthProviderClient {
    private final KakaoConfig kakaoConfig;
    private final GoogleConfig googleConfig;
    private final AppleJwtConfig appleConfig;

    private final OAuth2Util oAuth2Util;

    public String getKakaoAccessToken(String code, String origin) throws JsonProcessingException {
        String redirectUri = getKakaoRedirectUri(origin);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", kakaoConfig.getGrant_type());
        body.put("client_id", kakaoConfig.getClientId());
        body.put("redirect_uri", redirectUri);
        body.put("code", code);
        body.put("client_secret", kakaoConfig.getClient_secretId());
        return oAuth2Util.getAccessToken(kakaoConfig.getAccessTokenRequestUrl(), headers, body);
    }

    private String getKakaoRedirectUri(String origin) {
        return switch (origin) {
            case "production" -> kakaoConfig.getRedirect_uri_feProd();
            case "belocal" -> kakaoConfig.getRedirect_uri_beLocal();
            case "beprod" -> kakaoConfig.getRedirect_uri_beProd();
            case "bedev" -> kakaoConfig.getRedirect_uri_beDev();
            default -> kakaoConfig.getRedirect_uri_feLocal();
        };
    }

    public KakaoUserInfoProcessDto getKakaoUserInfo(String accessToken) throws JsonProcessingException  {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoConfig.getUser_info_uri(),
                HttpMethod.POST,
                request,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String id = jsonNode.get("id").asText(); // 카카오 고유식별자
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        return new KakaoUserInfoProcessDto(nickname, email, id);
    }

    public String getGoogleAccessToken(String code, String origin) throws JsonProcessingException {
        String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        String redirectUri = getGoogleRedirectUri(origin);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("client_id", googleConfig.getClientId());
        body.put("client_secret", googleConfig.getClient_secretId());
        body.put("code", decode);
        body.put("grant_type", googleConfig.getGrant_type());
        body.put("redirect_uri", redirectUri);
        return oAuth2Util.getAccessToken(googleConfig.getAccessTokenRequestUrl(), headers, body);
    }

    private String getGoogleRedirectUri(String origin) {
        return switch (origin) {
            case "production" -> googleConfig.getRedirect_uri_feProd();
            case "belocal" -> googleConfig.getRedirect_uri_beLocal();
            case "beprod" -> googleConfig.getRedirect_uri_beProd();
            case "bedev" -> googleConfig.getRedirect_uri_beDev();
            default -> googleConfig.getRedirect_uri_feLocal();
        };
    }

    public Map getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(googleConfig.getUser_info_uri(), HttpMethod.GET, request, Map.class);
        return response.getBody();
    }

    public String getAppleIdentityToken(String code, String origin){
        String redirectUri = getAppleRedirectUri(origin);
        String clientSecret = createClientSecret();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("grant_type", appleConfig.getGrant_type());
        bodyMap.put("code", code);
        bodyMap.put("redirect_uri", redirectUri);
        bodyMap.put("client_id", appleConfig.getClientId());
        bodyMap.put("client_secret", clientSecret);
        return oAuth2Util.getIdentityToken(appleConfig.getIdentityTokenRequestUri(), headers, bodyMap);
    }

    private String createClientSecret(){
        PrivateKey privateKey = getPrivateKey();
        long nowMillis  = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return Jwts.builder()
                .setHeaderParam("kid", appleConfig.getKeyId())
                .setIssuer(appleConfig.getTeamId())
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + 86400000))// 1일
                .setAudience("https://appleid.apple.com")
                .setSubject(appleConfig.getClientId())
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() {
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleConfig.getPrivate_key());

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }

    private String getAppleRedirectUri(String origin) {
        return switch (origin) {
            case "production" -> appleConfig.getRedirect_uri_feProd();
            default -> appleConfig.getRedirect_uri_beProd();
        };
    }
}
