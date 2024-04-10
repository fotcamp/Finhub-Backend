package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteRecentKeywordRequestDto {

    private Long id;

    public DeleteRecentKeywordRequestDto(Long id) {
        this.id = id;
    }
}
