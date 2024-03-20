package fotcamp.finhub.admin.dto.request;

import lombok.Getter;

@Getter
public class CreateBannerRequestDto {
    private String title;
    private String subTitle;
    private String landingPageUrl;
    private String s3ImgUrl;
    private String useYN;
}
