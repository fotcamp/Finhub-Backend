package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.MyScrapColumnProcessDto;
import fotcamp.finhub.main.dto.process.MyScrapTopicProcessDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class AllScrapResponseDto {

    private List<MyScrapTopicProcessDto> topicInfo;
    private List<MyScrapColumnProcessDto> columnInfo;

    public AllScrapResponseDto(List<MyScrapTopicProcessDto> topicInfo, List<MyScrapColumnProcessDto> columnInfo) {
        this.topicInfo = topicInfo;
        this.columnInfo = columnInfo;
    }
}
