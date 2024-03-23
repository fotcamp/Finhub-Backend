package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.TopicListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@NoArgsConstructor
public class OtherCategoriesResponseDto {

    Stream<TopicListProcessDto> category;

    public OtherCategoriesResponseDto(Stream<TopicListProcessDto> category) {
        this.category = category;
    }
}
