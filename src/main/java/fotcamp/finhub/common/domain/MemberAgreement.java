package fotcamp.finhub.common.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "Agreement")
public class MemberAgreement extends BaseEntity{ // 최초 회원가입 시간 정보 저장
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "privacy_policy")
    private boolean privacyPolicy; // 개인정보 처리 방침
    @Column(name = "terms_of_service")
    private boolean termsOfService; // 서비스 이용 약관

    private boolean pushYn = false;
    private LocalDateTime pushUpdateTime;

    public MemberAgreement(Member member) {
        this.member = member;
        this.pushYn = false;
    }

    public void updatePushYnState(boolean yn){
        this.pushYn = yn;
        this.pushUpdateTime = LocalDateTime.now();
    }

    public void agreeServiceTerms(boolean privacy_policy, boolean terms_of_service){
        this.privacyPolicy = privacy_policy;
        this.termsOfService = terms_of_service;
    }
}
