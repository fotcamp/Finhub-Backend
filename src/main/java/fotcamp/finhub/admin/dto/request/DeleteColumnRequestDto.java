package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteColumnRequestDto {

    private Long id;

    public DeleteColumnRequestDto(Long id) {
        this.id = id;
    }
}
