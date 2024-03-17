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

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final ListPath<MemberNotification, QMemberNotification> memberNotifications = this.<MemberNotification, QMemberNotification>createList("memberNotifications", MemberNotification.class, QMemberNotification.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final BooleanPath push_yn = createBoolean("push_yn");

    public final EnumPath<RoleType> role = createEnum("role", RoleType.class);

    public final NumberPath<Long> user_avatar_id = createNumber("user_avatar_id", Long.class);

    public final QUserAvatar userAvatar;

    public final NumberPath<Long> usertype_id = createNumber("usertype_id", Long.class);

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
        this.userAvatar = inits.isInitialized("userAvatar") ? new QUserAvatar(forProperty("userAvatar"), inits.get("userAvatar")) : null;
    }

}
