package fotcamp.finhub.main.dto.process.thirdTab;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class SearchPageInfoProcessDto {
    private int currentPage;  // 현재 페이지 번호
    private int totalPages;  // 전체 페이지 수
    private long totalResults;  // 전체 결과 수

    public SearchPageInfoProcessDto(int currentPage, int totalPages, long totalResults) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }
}
