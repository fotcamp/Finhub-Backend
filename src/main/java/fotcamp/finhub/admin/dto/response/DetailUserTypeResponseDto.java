package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.common.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailUserTypeResponseDto {
    private final Long id;
    private final String name;
    private final String avatarImgPath;
    private final String useYN;
}
