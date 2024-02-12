package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ModifyTopicRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "topic", cascade = CascadeType.PERSIST)
    private final List<Gpt> gptList = new ArrayList<>();

    // 토픽 카테고리 변경
    public void changeCategory(Category newCategory) {
        // 변경이 없는 경우에는 그냥 return
        if (this.category == newCategory) {
            return;
        }
        setCategory(newCategory);
    }

    public void addGpt(Gpt gpt) {
        gptList.add(gpt);
    }

    // 토픽 수정
    public void modifyTopic(ModifyTopicRequestDto modifyTopicRequestDto, Category category) {
        changeCategory(category);
        this.title = modifyTopicRequestDto.getTitle();
        this.definition = modifyTopicRequestDto.getDefinition();
        this.shortDefinition = modifyTopicRequestDto.getShortDefinition();
        this.thumbnailImgPath = modifyTopicRequestDto.getThumbnailImgPath();
    }

    // 이미지 url 생성 및 변경
    public void changeImgPath(String url) {
        this.thumbnailImgPath = url;
    }

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
