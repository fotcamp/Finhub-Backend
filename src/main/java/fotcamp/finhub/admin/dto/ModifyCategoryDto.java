package fotcamp.finhub.admin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModifyCategoryDto {
    private Long id;
    private String name;
    private String useYN;
    private List<ModifyTopicCategoryDto> topicList = new ArrayList<>();
}
