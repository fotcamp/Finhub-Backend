package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteAnnounceRequestDto {

    private Long id;

    public DeleteAnnounceRequestDto(Long id) {
        this.id = id;
    }
}
