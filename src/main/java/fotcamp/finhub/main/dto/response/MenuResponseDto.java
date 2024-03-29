package fotcamp.finhub.main.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

    private String defaultAvatar;
    private String nickname;
    private String job;


    public MenuResponseDto(String defaultAvatar, String nickname, String job) {
        this.defaultAvatar = defaultAvatar;
        this.nickname = nickname;
        this.job = job;
    }
}
