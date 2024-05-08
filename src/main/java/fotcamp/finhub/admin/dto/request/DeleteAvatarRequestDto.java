package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteAvatarRequestDto {

    private Long id;

    public DeleteAvatarRequestDto(Long id) {
        this.id = id;
    }
}
