package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapRequestDto {
    private Long type; // 1: 토픽 스크랩, GPT 칼럼 좋아요, 2: GPT 칼럼 스크랩, 댓글 좋아요
    private Long id;

    public ScrapRequestDto(Long id) {
        this.id = id;
    }
}
