package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.WeekPopularSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeekPopularKeywordRepository extends JpaRepository<WeekPopularSearch, Long> {
    @Query("SELECT ws FROM WeekPopularSearch ws WHERE ws.analysisDate = (SELECT MAX(ws2.analysisDate) FROM WeekPopularSearch ws2)")
    List<WeekPopularSearch> findWeekPopularSearchWithMaxAnalysisDate();
}

