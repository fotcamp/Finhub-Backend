package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.PopularSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PopularKeywordRepository extends JpaRepository<PopularSearch, Long> {

    Optional<PopularSearch> findByKeyword(String keyword);
    List<PopularSearch> findTop7ByOrderByFrequencyDesc();
}
