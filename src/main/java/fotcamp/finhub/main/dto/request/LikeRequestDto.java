package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequestDto {
    private Long type; // 1: 토픽, 2: gpt column
    private Long id;

    public LikeRequestDto(Long id) {
        this.id = id;
    }
}
