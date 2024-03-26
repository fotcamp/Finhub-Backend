package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ModifyUserTypeRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "userType")
    private List<Member> memberList = new ArrayList<>();


    public void modifyUserType(String name, String useYN, String avatarImgPath) {
        this.name = name;
        this.useYN = useYN;
        this.avatarImgPath = avatarImgPath;
    }

}