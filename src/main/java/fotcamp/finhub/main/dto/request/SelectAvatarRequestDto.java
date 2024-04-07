package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SelectAvatarRequestDto {

    private Long id;

    public SelectAvatarRequestDto(Long id) {
        this.id = id;
    }
}
