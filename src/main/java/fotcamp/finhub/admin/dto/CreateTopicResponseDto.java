package fotcamp.finhub.admin.dto;

import lombok.Getter;

@Getter
public class CreateTopicResponseDto {
    private final Long id;

    public CreateTopicResponseDto(Long id) {
        this.id = id;
    }
}
