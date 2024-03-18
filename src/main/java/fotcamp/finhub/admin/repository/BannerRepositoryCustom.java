package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BannerRepositoryCustom {
    Page<Banner> searchAllBannerFilterList(Pageable pageable, String useYN);
}
