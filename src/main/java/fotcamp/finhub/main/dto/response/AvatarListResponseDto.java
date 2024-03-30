package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.UserAvatarProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AvatarListResponseDto {

    private String defaultAvatar;
    private List<UserAvatarProcessDto> avatarList;

    public AvatarListResponseDto(String defaultAvatar, List<UserAvatarProcessDto> avatarList) {
        this.defaultAvatar = defaultAvatar;
        this.avatarList = avatarList;
    }
}
