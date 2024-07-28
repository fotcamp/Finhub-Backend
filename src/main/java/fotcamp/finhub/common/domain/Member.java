package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERTYPE_ID")
    private UserType userType;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PROFILE_NICKNAME", nullable = false)
    private String name;

    private String nickname;
    private boolean pushYn;
    private String fcmToken;
    private LocalDateTime fcmTokenCreatedAt;
    private String provider; // OAuth 가입 방법 ( google , kakao )

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private RoleType role;

    // userAvatar 1대1 연관관계 ( 주인은 member : 유저 아바타를 등록하지 않은 경우 null로 처리 )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_AVATAR_ID")
    private UserAvatar userAvatar;

    // calendarEmoticon 1대1 연관관계 ( 주인은 member : 캘린더 이모티콘을 등록하지 않은 경우 null로 처리 )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CALENDAR_EMOTICON_ID")
    private CalendarEmoticon calendarEmoticon;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberNotification> memberNotificationList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<MemberQuiz> quizList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberScrap> memberScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<RecentSearch> recentSearchList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private RefreshToken refreshToken;

    public Member(String email, String name, String provider){
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.role = RoleType.ROLE_USER;
    }

    public void updateFcmToken(String fcmToken){

        this.fcmToken = fcmToken;
        this.fcmTokenCreatedAt = LocalDateTime.now();
    }

    @Builder
    public Member(UserType userType, String email, String name, String nickname, boolean pushYn, String fcmToken, RoleType role, UserAvatar userAvatar, List<MemberNotification> memberNotificationList, List<MemberScrap> memberScrapList, List<RecentSearch> recentSearchList) {
        this.userType = userType;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.pushYn = pushYn;
        this.fcmToken = fcmToken;
        this.role = RoleType.ROLE_USER;
        this.userAvatar = userAvatar;
        this.memberNotificationList = memberNotificationList;
        this.memberScrapList = memberScrapList;
        this.recentSearchList = recentSearchList;
    }

    public void updateNickname(String newNickname){
        this.nickname = newNickname;
    }

    public void updateJob(UserType userType){
        this.userType = userType;
    }

    public void updateAvatar(UserAvatar userAvatar){
        this.userAvatar = userAvatar;
    }

    public void removeScrap(MemberScrap memberScrap){
        this.memberScrapList.remove(memberScrap);
    }

    public void removeUserAvatar(){
        this.userAvatar = null;
    }

    public void addMemberQuiz(MemberQuiz memberQuiz) {
        quizList.add(memberQuiz);
    }

    public void updatePushYN(boolean isYN){
        this.pushYn = isYN;
    }

    public void updateCalendarEmoticon(CalendarEmoticon calendarEmoticon) {
        this.calendarEmoticon = calendarEmoticon;
    }

    public void removeUsertype() {this.userType = null;}
    public void removeCalendarEmoticon(){this.calendarEmoticon = null;}
    public void removeFcmToken() {
        this.fcmToken = null;
        this.fcmTokenCreatedAt = null;
    }
}
