package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.SearchResultListDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchResponseDto {

    private List<SearchResultListDto> dto; // 검색결과 목록
    private int currentPage;  // 현재 페이지 번호
    private int totalPages;  // 전체 페이지 수
    private long totalResults;  // 전체 결과 수

    public SearchResponseDto(List<SearchResultListDto> dto, int currentPage, int totalPages, long totalResults) {
        this.dto = dto;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }
}
