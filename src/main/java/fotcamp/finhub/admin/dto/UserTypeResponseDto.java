package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.UserType;
import lombok.Data;

@Data
public class UserTypeResponseDto {
    private final Long id;
    private String name;
    private String avatar;
    private String useYN;

    public UserTypeResponseDto(UserType userType) {
        this.id = userType.getId();
        this.name = userType.getName();
        this.avatar = userType.getAvatarImgPath();
        this.useYN = userType.getUseYN();
    }
}
