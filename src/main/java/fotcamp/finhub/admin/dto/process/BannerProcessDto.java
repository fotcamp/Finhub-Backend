package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Banner;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BannerProcessDto {
    private final Long id;
    private final String title;
    private final String subTitle;
    private final String useYN;
    private final String createdBy;
    private final LocalDateTime createdTime;
    private final LocalDateTime modifiedTime;

    public BannerProcessDto(Banner banner) {
        this.id = banner.getId();
        this.title = banner.getTitle();
        this.subTitle = banner.getSubTitle();
        this.useYN = banner.getUseYN();
        this.createdBy = banner.getCreatedBy();
        this.createdTime = banner.getCreatedTime();
        this.modifiedTime = banner.getModifiedTime();
    }
}
