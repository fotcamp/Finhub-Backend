package fotcamp.finhub.admin.dto;

import lombok.Getter;

@Getter
public class CreateGptContentDto {
    private Long topicId;
    private Long usertypeId;
    private String createdBy;
}
