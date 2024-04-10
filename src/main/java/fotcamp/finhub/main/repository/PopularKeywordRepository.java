package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.PopularSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PopularKeywordRepository extends JpaRepository<PopularSearch, Long> {

    Optional<PopularSearch> findByKeyword(String keyword);

    @Query("SELECT p.keyword FROM PopularSearch p WHERE p.date BETWEEN :startDate AND :endDate ORDER BY p.frequency DESC, p.keyword ASC")
    List<String> findTopKeywordsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}
