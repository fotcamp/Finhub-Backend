package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SelectUsertypeRequestDto {

    private Long id;

    public SelectUsertypeRequestDto(Long id) {
        this.id = id;
    }
}
