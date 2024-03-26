package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailNextTopicProcessDto {

    private Long id;
    private String title;

    public DetailNextTopicProcessDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
