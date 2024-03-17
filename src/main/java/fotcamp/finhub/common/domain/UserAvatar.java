package fotcamp.finhub.common.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserAvatar extends BaseEntity{

    @Id
    @Column(name = "USER_AVATAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avatar_img_path;

    private String createdBy;

    @OneToMany(mappedBy = "userAvatar") // 1대1 연관관계 주인은 member
    private List<Member> memberList = new ArrayList<>();


}
