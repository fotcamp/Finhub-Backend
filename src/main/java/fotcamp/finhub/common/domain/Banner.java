package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ModifyBannerRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Banner extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subTitle;
    private String bannerImageUrl;
    private String landingPageUrl;
    private String createdBy;
    private String useYN;


    // 이미지 url 생성 및 변경
    public void changeImgPath(String url) {
        this.bannerImageUrl = url;
    }

    // Banner 수정
    public void modifyBanner(ModifyBannerRequestDto dto, String role) {
        this.title = dto.getTitle();
        this.subTitle = dto.getSubTitle();
        this.landingPageUrl = dto.getLandingPageUrl();
        this.useYN = dto.getUseYN();
        this.createdBy = role;
    }
}
