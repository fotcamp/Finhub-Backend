package fotcamp.finhub.admin.domain;

import fotcamp.finhub.common.domain.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_ID")
    private Long memberId; // 시큐리티 필터 과정에서 member 엔티티와 이름이 동일해야함. 이름 협의 필요

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private RoleType role;

    @Column(name = "FCM_TOKEN")
    private String fcmToken;

    @Column(name = "ADMIN_UUID")
    private String managerUuid;

    @Builder
    public Manager(Long memberId, String email, String name, String password, String fcmToken) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = RoleType.ROLE_BE;
        this.fcmToken = fcmToken;
        this.managerUuid = "FOTCAMP";
    }

    public void updateFcmToken(String token){
        this.fcmToken = token;
    }
}
