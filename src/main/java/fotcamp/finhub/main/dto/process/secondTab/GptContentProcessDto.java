package fotcamp.finhub.main.dto.process.secondTab;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptContentProcessDto {

    private String name;
    private String content;

    public GptContentProcessDto(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
