package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.UserType;
import lombok.Getter;

@Getter
public class UserTypeProcessDto {
    private final Long id;
    private final String name;
    private final String useYN;
    private final String avatarImgPath;

    public UserTypeProcessDto(Long id, String name, String useYN, String avatarImgPath) {
        this.id = id;
        this.name = name;
        this.useYN = useYN;
        this.avatarImgPath = avatarImgPath;
    }
}
