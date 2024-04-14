package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapRequestDto {
    private Long type; // 1: 토픽, 2: gpt column
    private Long id;

    public ScrapRequestDto(Long id) {
        this.id = id;
    }
}
