package fotcamp.finhub.main.scheduler;

import fotcamp.finhub.main.service.PopularSearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchTrendAnalysisScheduler {
    private final PopularSearchService popularSearchService;

    @Scheduled(cron = "0 1 0 * * MON", zone = "Asia/Seoul")
    @Transactional
    public void analyzeAndSaveSearchTrends() {
        popularSearchService.analyzeWeeklySearchTrends();
    }
}
