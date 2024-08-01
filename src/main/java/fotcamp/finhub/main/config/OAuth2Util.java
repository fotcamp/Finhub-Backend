package fotcamp.finhub.main.config;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class OAuth2Util {

    public String getAccessToken(String tokenUrl, HttpHeaders headers, Map<String, String> bodyMap) { // 카카오로그인, 구글로그인
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.setAll(bodyMap);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    public String getIdentityToken(String tokenUrl, HttpHeaders headers, Map<String, String> bodyMap) { // 애플로그인
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.setAll(bodyMap);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        return (String) response.getBody().get("id_token");
    }

    public Map getUserInfo(String userInfoUrl, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
        return response.getBody();
    }
}
