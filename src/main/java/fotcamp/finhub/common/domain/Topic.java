package fotcamp.finhub.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Topic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String definition;

    private String shortDefinition;

    private String thumbnailImgPath;

    @Builder.Default
    private String useYN = "N";

    private String createdBy;

    // 연관관계 편의 메서드
    public void setCategory(Category category) {
        // 기존 카테고리 있을 시 제거
        if (this.category != null) {
            this.category.getTopics().remove(this);
        }
        this.category = category;
        category.addTopic(this);
    }

}
