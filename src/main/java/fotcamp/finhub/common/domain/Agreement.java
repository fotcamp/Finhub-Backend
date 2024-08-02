package fotcamp.finhub.common.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private boolean privacy_policy;
    private boolean terms_of_service;

    public Agreement(Long memberId, boolean privacy_policy, boolean terms_of_service) {
        this.memberId = memberId;
        this.privacy_policy = privacy_policy;
        this.terms_of_service = terms_of_service;
    }
}
