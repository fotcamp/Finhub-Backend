package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.UserType;
import lombok.Getter;

@Getter
public class DetailUserTypeResponseDto {
    private final Long id;
    private final String name;
    private final String avatarImgPath;
    private final String useYN;

    public DetailUserTypeResponseDto(UserType userType) {
        this.id = userType.getId();
        this.name = userType.getName();
        this.avatarImgPath = userType.getAvatarImgPath();
        this.useYN = userType.getUseYN();
    }
}
