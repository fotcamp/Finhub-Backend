package fotcamp.finhub.main.dto.response.thirdTab;


import fotcamp.finhub.main.dto.process.thirdTab.SearchColumnResultListProcessDto;
import fotcamp.finhub.main.dto.process.thirdTab.SearchPageInfoProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchColumnResponseDto {

    private List<SearchColumnResultListProcessDto> result;
    private SearchPageInfoProcessDto pageInfo;

    public SearchColumnResponseDto(List<SearchColumnResultListProcessDto> result, SearchPageInfoProcessDto pageInfo) {
        this.result = result;
        this.pageInfo = pageInfo;
    }
}
