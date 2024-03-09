package fotcamp.finhub.main.dto.response;


import lombok.Getter;

@Getter
public class KakaoAccessTokenResponse {

    private String tokenType;
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private Integer refreshTokenExpiresIn;

}
