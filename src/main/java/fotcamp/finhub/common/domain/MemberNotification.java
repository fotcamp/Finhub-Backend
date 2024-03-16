package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberNotification {


    @Id
    @Column(name = "MEMBER_NOTI_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNotificationId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTI_ID")
    private Notification notification;

    private boolean isRead; // default는 false

    private LocalDateTime sentAt;
    private LocalDateTime receivedAt;

    @PrePersist
    protected void onCreated(){
        sentAt = LocalDateTime.now();
    }

}
