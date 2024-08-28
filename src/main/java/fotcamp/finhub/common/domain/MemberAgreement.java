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

    private boolean privacy_policy; // 개인정보 처리 방침
    private boolean terms_of_service; // 서비스 이용 약관

    // 앱 푸시 허용 해제 컬럼
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
        this.privacy_policy = privacy_policy;
        this.terms_of_service = terms_of_service;
    }
}
