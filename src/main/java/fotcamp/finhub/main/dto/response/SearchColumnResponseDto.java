package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.SearchColumnResultListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchColumnResponseDto {

    private List<SearchColumnResultListProcessDto> result;
    private int currentPage;  // 현재 페이지 번호
    private int totalPages;  // 전체 페이지 수
    private long totalResults;  // 전체 결과 수

    public SearchColumnResponseDto(List<SearchColumnResultListProcessDto> result, int currentPage, int totalPages, long totalResults) {
        this.result = result;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }
}
