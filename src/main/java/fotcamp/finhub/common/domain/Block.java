package fotcamp.finhub.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "BLOCK_MEMBER_ID")
    private Member blockMember;

    @Builder
    public Block(Member member, Member blockMember) {
        this.member = member;
        this.blockMember = blockMember;
    }
}

