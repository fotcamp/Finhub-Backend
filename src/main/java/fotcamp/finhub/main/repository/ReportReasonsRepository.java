package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.ReportReasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportReasonsRepository extends JpaRepository<ReportReasons, Long> {
    List<ReportReasons> findAllByUseYnOrderByIdAsc(String useYN);
    List<ReportReasons> findAllByOrderByIdAsc();

    Optional<ReportReasons> findByReason(String reason);
}
