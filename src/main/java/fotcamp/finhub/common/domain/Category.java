package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ModifyCategoryRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String thumbnailImgPath;

    @Builder.Default
    private String useYN = "N";

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private final List<Topic> topics = new ArrayList<>();

    public void modifyNameUseYN(ModifyCategoryRequestDto modifyCategoryRequestDto) {
        this.name = modifyCategoryRequestDto.name();
        this.useYN = modifyCategoryRequestDto.useYN();
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    // 이미지 url 생성 및 변경
    public void changeImgPath(String url) {
        this.thumbnailImgPath = url;
    }
}
