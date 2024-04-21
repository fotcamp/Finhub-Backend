package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteTopicRequestDto {

    private Long id;

    public DeleteTopicRequestDto(Long id) {
        this.id = id;
    }
}
