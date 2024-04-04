package fotcamp.finhub.main.dto.response.secondTab;


import fotcamp.finhub.main.dto.process.secondTab.GptContentProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptContentResponseDto {

    private GptContentProcessDto contentInfo;

    public GptContentResponseDto(GptContentProcessDto contentInfo) {
        this.contentInfo = contentInfo;
    }
}
