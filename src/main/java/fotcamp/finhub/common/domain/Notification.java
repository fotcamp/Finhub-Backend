package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification {

    @Id
    @Column(name = "NOTI_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;
    private String url;

    @OneToMany(mappedBy = "notification")
    private List<MemberNotification> memberNotificationList = new ArrayList<>();
}
