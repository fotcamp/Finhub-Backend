package fotcamp.finhub.main.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class RecentSearchResponseDto {

    private Long id;
    private String keyword;

    public RecentSearchResponseDto(Long searchId, String keyword) {
        this.id = searchId;
        this.keyword = keyword;
    }
}
