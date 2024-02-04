package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ModifyUserTypeRequestDto;
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

    public void modifyUserType(ModifyUserTypeRequestDto modifyUserTypeRequestDto) {
        this.name = modifyUserTypeRequestDto.name();
        this.avatarImgPath = modifyUserTypeRequestDto.avatarImgPath();
        this.useYN = modifyUserTypeRequestDto.useYN();
    }
}
