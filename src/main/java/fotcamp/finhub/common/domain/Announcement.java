package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ModifyAnnounceRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announcement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Column(name = "created_by")
    private String createdBy;

    public void updateAnnounce(ModifyAnnounceRequestDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public Announcement(String title, String content, String createdBy) {
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
    }
}
