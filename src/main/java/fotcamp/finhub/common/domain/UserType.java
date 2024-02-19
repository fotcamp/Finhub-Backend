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
        this.useYN = modifyUserTypeRequestDto.useYN();
    }

    // 이미지 url 생성 및 변경
    public void changeImgPath(String url) {
        this.avatarImgPath = url;
    }
}
