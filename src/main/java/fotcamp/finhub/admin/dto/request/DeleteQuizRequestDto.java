package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteQuizRequestDto {

    private Long id;

    public DeleteQuizRequestDto(Long id) {
        this.id = id;
    }
}
