package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.thirdTab.SearchPageInfoProcessDto;
import fotcamp.finhub.main.dto.process.thirdTab.SearchTopicResultListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchTopicResponseDto {

    private List<SearchTopicResultListProcessDto> result; // 검색결과 목록
    private SearchPageInfoProcessDto pageInfo;

    public SearchTopicResponseDto(List<SearchTopicResultListProcessDto> result, SearchPageInfoProcessDto pageInfo) {
        this.result = result;
        this.pageInfo = pageInfo;
    }
}
