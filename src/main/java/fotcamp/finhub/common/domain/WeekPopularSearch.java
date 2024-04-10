package fotcamp.finhub.common.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WeekPopularSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate analysisDate; // 분석 날짜
    private String keyword; // 키워드
    private String trend; // 트렌드: New, Increased, Decreased

    @Builder
    public WeekPopularSearch(String keyword, String trend) {
        this.analysisDate = LocalDate.now();
        this.keyword = keyword;
        this.trend = trend;
    }


}
