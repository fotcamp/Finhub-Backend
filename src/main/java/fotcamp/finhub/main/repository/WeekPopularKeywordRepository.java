package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.PopularSearch;
import fotcamp.finhub.common.domain.WeekPopularSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeekPopularKeywordRepository extends JpaRepository<WeekPopularSearch, Long> {
    List<WeekPopularSearch> findAllByOrderByIdAsc();
}

