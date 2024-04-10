package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.GptColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GptColumnRepository extends JpaRepository<GptColumn, Long> {

    Page<GptColumn> findByTitleContaining(String keyword, Pageable pageable);
    Page<GptColumn> findByContentContaining(String keyword, Pageable pageable);
    Page<GptColumn> findByTitleContainingOrContentContaining(String titleKeyword, String summaryKeyword, Pageable pageable);
}
