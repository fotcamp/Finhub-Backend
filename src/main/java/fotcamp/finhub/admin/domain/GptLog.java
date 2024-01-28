package fotcamp.finhub.admin.domain;

import fotcamp.finhub.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GptLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Long topicId;

    @Column(nullable = false)
    private Long usertypeId;

    @Column(nullable = false)
    private String question;

    @Lob
    @Column(nullable = false)
    private String answer;

    private String createdBy;

}
