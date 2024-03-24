package fotcamp.finhub.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CalendarEmoticon extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emoticon_img_path;

    private String createdBy;

    @Builder
    public CalendarEmoticon(String emoticon_img_path, String createdBy) {
        this.emoticon_img_path = emoticon_img_path;
        this.createdBy = createdBy;
    }
}
