package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.ModifyUserTypeDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String avatarImgPath;

    @Builder.Default
    private String useYN = "N";

    public void modifyUserType(ModifyUserTypeDto modifyUserTypeDto) {
        this.name = modifyUserTypeDto.getName();
        this.avatarImgPath = modifyUserTypeDto.getAvatar();
        this.useYN = modifyUserTypeDto.getUseYN();
    }
}
