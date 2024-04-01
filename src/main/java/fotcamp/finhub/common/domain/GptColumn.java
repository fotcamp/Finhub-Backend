package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ModifyGptColumnRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GptColumn extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String summary;
    private String content;
    private String backgroundUrl;
    private String useYN;
    private String createdBy;

    @OneToMany(mappedBy = "gptColumn", cascade = CascadeType.PERSIST)
    private final List<TopicGptColumn> topicGptColumnList = new ArrayList<>();

    public void addTopicGptColumn(TopicGptColumn topicGptColumn) {
        topicGptColumnList.add(topicGptColumn);
    }

    public void modifyGptColumn(ModifyGptColumnRequestDto gptColumn, String url, String role) {
        this.title = gptColumn.getTitle();
        this.summary = gptColumn.getSummary();
        this.content = gptColumn.getContent();
        this.backgroundUrl = url;
        this.useYN = gptColumn.getUseYN();
        this.createdBy = role;
    }

}
