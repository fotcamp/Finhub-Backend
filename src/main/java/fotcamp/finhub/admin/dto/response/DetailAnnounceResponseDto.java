package fotcamp.finhub.admin.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class DetailAnnounceResponseDto {
    private Long id;
    private String title;
    private String content;

    public DetailAnnounceResponseDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
