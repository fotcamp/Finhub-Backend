package fotcamp.finhub.admin.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ModifyCategoryDto {
    private Long id;
    private String name;
    private String thumbnailImgPath;
    private String useYN;
    private List<ModifyTopicCategoryDto> topicList = new ArrayList<>();
}
