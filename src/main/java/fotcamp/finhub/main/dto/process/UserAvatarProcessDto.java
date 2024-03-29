package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAvatarProcessDto {

    private Long id;
    private String imgUrl;

    public UserAvatarProcessDto(Long id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }
}
