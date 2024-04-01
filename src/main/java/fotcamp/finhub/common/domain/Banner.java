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
    private String bannerType;
    private String landingPageUrl;
    private String createdBy;
    private String useYN;

    // Banner 수정
    public void modifyBanner(String title, String subTitle, String landingPageUrl, String bannerType, String bannerImageUrl, String useYN, String role) {
        this.title = title;
        this.subTitle = subTitle;
        this.landingPageUrl = landingPageUrl;
        this.bannerImageUrl = bannerImageUrl;
        this.bannerType = bannerType;
        this.useYN = useYN;
        this.createdBy = role;
    }
}
