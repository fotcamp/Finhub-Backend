package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.GptColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GptColumnRepository extends JpaRepository<GptColumn, Long> {

    Page<GptColumn> findByUseYNAndTitleContaining(String useYN, String keyword, Pageable pageable);
    Page<GptColumn> findByUseYNAndContentContaining(String useYN, String keyword, Pageable pageable);
    Page<GptColumn> findByUseYNAndTitleContainingOrContentContaining(String useYN, String titleKeyword, String summaryKeyword, Pageable pageable);
}
