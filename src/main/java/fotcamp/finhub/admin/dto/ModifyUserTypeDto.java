package fotcamp.finhub.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifyUserTypeDto {
    private Long id;
    private String name;
    private String avatar;
    private String useYN;

}
