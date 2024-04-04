package fotcamp.finhub.main.dto.process.login;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class UserInfoProcessDto {

    private String nickname;
    private String email;
    private String avatarUrl;
    private String userType;
    private String userTypeUrl;
    private Boolean pushYN;

    public UserInfoProcessDto(String nickname, String email, String avatarUrl, String userType, String userTypeUrl, Boolean pushYN) {
        this.nickname = nickname;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.userType = userType;
        this.userTypeUrl = userTypeUrl;
        this.pushYN = pushYN;
    }
}
