package fotcamp.finhub.main.dto.process.login;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoProcessDto {

    private String name;
    private String email;
    private String uuid;
}
