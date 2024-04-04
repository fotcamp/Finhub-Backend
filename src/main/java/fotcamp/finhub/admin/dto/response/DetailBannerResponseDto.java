package fotcamp.finhub.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DetailBannerResponseDto {
    private final Long id;
    private final String title;
    private final String subTitle;
    private final String landingPageUrl;
    private final String bannerType;
    private final String useYN;
    private final String createdBy;
    private final String bannerImageUrl;
    private final LocalDateTime createdTime;
    private final LocalDateTime modifiedTime;
}
