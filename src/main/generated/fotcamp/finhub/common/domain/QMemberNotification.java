package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberNotification is a Querydsl query type for MemberNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberNotification extends EntityPathBase<MemberNotification> {

    private static final long serialVersionUID = -1926766034L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberNotification memberNotification = new QMemberNotification("memberNotification");

    public final BooleanPath isRead = createBoolean("isRead");

    public final QMember member;

    public final NumberPath<Long> memberNotificationId = createNumber("memberNotificationId", Long.class);

    public final QNotification notification;

    public final DateTimePath<java.time.LocalDateTime> receivedAt = createDateTime("receivedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> sentAt = createDateTime("sentAt", java.time.LocalDateTime.class);

    public QMemberNotification(String variable) {
        this(MemberNotification.class, forVariable(variable), INITS);
    }

    public QMemberNotification(Path<? extends MemberNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberNotification(PathMetadata metadata, PathInits inits) {
        this(MemberNotification.class, metadata, inits);
    }

    public QMemberNotification(Class<? extends MemberNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.notification = inits.isInitialized("notification") ? new QNotification(forProperty("notification")) : null;
    }

}

