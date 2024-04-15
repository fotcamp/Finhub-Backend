package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostsScrap extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gpt_column_id")
    private GptColumn gptColumn;

    public PostsScrap(GptColumn gptColumn, Member member) {
        this.member = member;
        this.gptColumn = gptColumn;
    }

}
