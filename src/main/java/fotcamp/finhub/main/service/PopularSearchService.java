package fotcamp.finhub.main.service;

import fotcamp.finhub.common.domain.WeekPopularSearch;
import fotcamp.finhub.main.repository.PopularKeywordRepository;
import fotcamp.finhub.main.repository.WeekPopularKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PopularSearchService {
    private final PopularKeywordRepository popularKeywordRepository;
    private final WeekPopularKeywordRepository weekPopularKeywordRepository;

    @Transactional(readOnly = true)
    public void analyzeWeeklySearchTrends() {
        LocalDate today = LocalDate.now();
        LocalDate lastMonday = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate twoWeeksAgoMonday = today.minusWeeks(2).with(DayOfWeek.MONDAY);
        Pageable topFive = PageRequest.of(0, 5);

        List<String> lastWeekSearches = popularKeywordRepository.findTopKeywordsBetweenDates(lastMonday, lastMonday.plusDays(6), topFive);
        List<String> twoWeeksAgoSearches = popularKeywordRepository.findTopKeywordsBetweenDates(twoWeeksAgoMonday, twoWeeksAgoMonday.plusDays(6), topFive);

        Map<String, String> searchTrends = new LinkedHashMap<>();

        for (int i = 0; i < lastWeekSearches.size(); i++) {
            String search = lastWeekSearches.get(i);
            if (twoWeeksAgoSearches.contains(search)) {
                int lastIndex = twoWeeksAgoSearches.indexOf(search);
                if (i < lastIndex) {
                    searchTrends.put(search, "Increased");
                } else if (i > lastIndex) {
                    searchTrends.put(search, "Decreased");
                } else {
                    searchTrends.put(search, "Stable");
                }
            } else {
                searchTrends.put(search, "New");
            }
        }

        // searchTrends 맵을 반복하여 새로운 데이터 저장
        searchTrends.forEach((keyword, trend) -> {
            WeekPopularSearch weekPopularSearch = WeekPopularSearch.builder()
                    .keyword(keyword)
                    .trend(trend)
                    .build();
            weekPopularKeywordRepository.save(weekPopularSearch);
        });

    }

}
