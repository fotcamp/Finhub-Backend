package fotcamp.finhub.main.dto.process.secondTab;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class DetailNextTopicProcessDto {

    private Long id;
    private String title;
    private String img_path;

    public DetailNextTopicProcessDto(Long id, String title, String img_path) {
        this.id = id;
        this.title = title;
        this.img_path = img_path;
    }

}
