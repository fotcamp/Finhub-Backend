package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecentSearch {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String keyword;
    private LocalDateTime localDateTime;

    public RecentSearch(Member member, String keyword, LocalDateTime localDateTime) {
        this.member = member;
        this.keyword = keyword;
        this.localDateTime = localDateTime;
    }

    public void updateRecord(LocalDateTime localDateTime){
        this.localDateTime = localDateTime;
    }
}
