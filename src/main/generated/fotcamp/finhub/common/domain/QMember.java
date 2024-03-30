package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -369204829L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final QCalendarEmoticon calendarEmoticon;

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final ListPath<MemberNotification, QMemberNotification> memberNotificationList = this.<MemberNotification, QMemberNotification>createList("memberNotificationList", MemberNotification.class, QMemberNotification.class, PathInits.DIRECT2);

    public final ListPath<MemberScrap, QMemberScrap> memberScrapList = this.<MemberScrap, QMemberScrap>createList("memberScrapList", MemberScrap.class, QMemberScrap.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final BooleanPath push_yn = createBoolean("push_yn");

    public final ListPath<MemberQuiz, QMemberQuiz> quizList = this.<MemberQuiz, QMemberQuiz>createList("quizList", MemberQuiz.class, QMemberQuiz.class, PathInits.DIRECT2);

    public final ListPath<RecentSearch, QRecentSearch> recentSearchList = this.<RecentSearch, QRecentSearch>createList("recentSearchList", RecentSearch.class, QRecentSearch.class, PathInits.DIRECT2);

    public final QRefreshToken refreshToken;

    public final EnumPath<RoleType> role = createEnum("role", RoleType.class);

    public final QUserAvatar userAvatar;

    public final QUserType userType;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.calendarEmoticon = inits.isInitialized("calendarEmoticon") ? new QCalendarEmoticon(forProperty("calendarEmoticon")) : null;
        this.refreshToken = inits.isInitialized("refreshToken") ? new QRefreshToken(forProperty("refreshToken"), inits.get("refreshToken")) : null;
        this.userAvatar = inits.isInitialized("userAvatar") ? new QUserAvatar(forProperty("userAvatar")) : null;
        this.userType = inits.isInitialized("userType") ? new QUserType(forProperty("userType")) : null;
    }

}

