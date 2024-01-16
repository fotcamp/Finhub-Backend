package fotcamp.finhub.common.domain;

import fotcamp.finhub.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Gpt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Long topicId;

    @Column(nullable = false)
    private Long userTypeId;

    @Column(nullable = false)
    private String content;

    private String createdBy;
}
