package fotcamp.finhub.common.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserAvatar extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avatar_img_path;

    private String createdBy;

    @Builder
    public UserAvatar(String avatar_img_path, String createdBy) {
        this.avatar_img_path = avatar_img_path;
        this.createdBy = createdBy;
    }
}
