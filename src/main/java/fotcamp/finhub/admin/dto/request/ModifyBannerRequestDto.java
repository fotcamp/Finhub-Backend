package fotcamp.finhub.admin.dto.request;

import lombok.Getter;

@Getter
public class ModifyBannerRequestDto {
    private Long id;
    private String title;
    private String subTitle;
    private String landingPageUrl;
    private String useYN;
}
