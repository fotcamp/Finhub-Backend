package fotcamp.finhub.common.dto.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 로직 내부에서 두 가지 토큰을 저장해 둘 DTO */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

}
