package fotcamp.finhub.common.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PopularSearch {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;
    private Long frequency;

    public PopularSearch(String keyword) {
        this.keyword = keyword;
        this.frequency = 0L;
    }

    public void plusFrequency(){
        this.frequency += 1L;
    }
}
