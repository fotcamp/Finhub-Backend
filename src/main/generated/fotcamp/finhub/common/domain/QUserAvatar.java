package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAvatar is a Querydsl query type for UserAvatar
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAvatar extends EntityPathBase<UserAvatar> {

    private static final long serialVersionUID = -1422229619L;

    public static final QUserAvatar userAvatar = new QUserAvatar("userAvatar");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath avatar_img_path = createString("avatar_img_path");

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Member, QMember> memberList = this.<Member, QMember>createList("memberList", Member.class, QMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public QUserAvatar(String variable) {
        super(UserAvatar.class, forVariable(variable));
    }

    public QUserAvatar(Path<? extends UserAvatar> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAvatar(PathMetadata metadata) {
        super(UserAvatar.class, metadata);
    }

}

