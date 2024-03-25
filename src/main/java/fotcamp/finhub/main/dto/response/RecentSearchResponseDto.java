package fotcamp.finhub.main.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecentSearchResponseDto {

    private Long searchId;
    private String keyword;

    public RecentSearchResponseDto(Long searchId, String keyword) {
        this.searchId = searchId;
        this.keyword = keyword;
    }
}
