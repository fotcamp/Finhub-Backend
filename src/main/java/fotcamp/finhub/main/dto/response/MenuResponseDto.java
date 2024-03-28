package fotcamp.finhub.main.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

    private String nickname;
    private String email;

    public MenuResponseDto(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
