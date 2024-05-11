package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.QuitReasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuitReasonsRepository extends JpaRepository<QuitReasons, Long> {
    List<QuitReasons> findByUseYn(String useYn);
}
