package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = -1387186892L;

    public static final QNotification notification = new QNotification("notification");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<MemberNotification, QMemberNotification> memberNotificationList = this.<MemberNotification, QMemberNotification>createList("memberNotificationList", MemberNotification.class, QMemberNotification.class, PathInits.DIRECT2);

    public final StringPath message = createString("message");

    public final StringPath title = createString("title");

    public QNotification(String variable) {
        super(Notification.class, forVariable(variable));
    }

    public QNotification(Path<? extends Notification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotification(PathMetadata metadata) {
        super(Notification.class, metadata);
    }

}

