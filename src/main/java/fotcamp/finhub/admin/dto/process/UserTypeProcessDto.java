package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.UserType;
import lombok.Getter;

@Getter
public class UserTypeProcessDto {
    private final Long id;
    private final String name;
    private final String avatarImgPath;
    private final String useYN;

    public UserTypeProcessDto(UserType userType) {
        this.id = userType.getId();
        this.name = userType.getName();
        this.avatarImgPath = userType.getAvatarImgPath();
        this.useYN = userType.getUseYN();
    }
}
