package fotcamp.finhub.admin.dto;

import lombok.Data;

@Data
public class CreateTopicDto {
    private Long categoryId;
    private String title;
    private String definition;
    private String shortDefinition;
    private String thumbnail;
    private String useYN;
    private String createdBy;
}
