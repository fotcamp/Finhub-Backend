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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAvatar userAvatar = new QUserAvatar("userAvatar");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath avatar_img_path = createString("avatar_img_path");

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final NumberPath<Long> userAvatarId = createNumber("userAvatarId", Long.class);

    public QUserAvatar(String variable) {
        this(UserAvatar.class, forVariable(variable), INITS);
    }

    public QUserAvatar(Path<? extends UserAvatar> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAvatar(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAvatar(PathMetadata metadata, PathInits inits) {
        this(UserAvatar.class, metadata, inits);
    }

    public QUserAvatar(Class<? extends UserAvatar> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

