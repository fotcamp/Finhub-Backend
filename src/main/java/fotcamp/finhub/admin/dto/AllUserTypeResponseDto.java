package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.UserType;
import lombok.Data;

@Data
public class AllUserTypeResponseDto {
    private final Long id;
    private String name;
    private String avatar;
    private String useYN;

    public AllUserTypeResponseDto(UserType userType) {
        this.id = userType.getId();
        this.name = userType.getName();
        this.avatar = userType.getAvatarImgPath();
        this.useYN = userType.getUseYN();
    }
}
