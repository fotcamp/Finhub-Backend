package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteUsertypeRequestDto {

    private Long id;

    public DeleteUsertypeRequestDto(Long id) {
        this.id = id;
    }
}
