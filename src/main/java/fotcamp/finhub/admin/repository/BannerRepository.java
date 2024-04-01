package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    // useYN이 'Y'인 엔티티 중에서 id를 기준으로 최신순으로 상위 3개를 조회하는 메서드
    List<Banner> findTop3ByUseYNOrderByIdDesc(String useYN);
}
