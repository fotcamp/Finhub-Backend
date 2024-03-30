package fotcamp.finhub.main.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {

    private Long avatarId;
    private String imgUrl;
    private String email;

    public ProfileResponseDto(Long avatarId, String imgUrl, String email) {
        this.avatarId = avatarId;
        this.imgUrl = imgUrl;
        this.email = email;
    }
}
