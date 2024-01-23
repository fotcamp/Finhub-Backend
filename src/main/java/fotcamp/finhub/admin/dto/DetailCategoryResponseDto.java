package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Topic;
import lombok.Data;

@Data
public class DetailCategoryResponseDto {
    private Long id;
    private String title;
    private String useYN;

    public DetailCategoryResponseDto(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.useYN = topic.getUseYN();
    }
}
