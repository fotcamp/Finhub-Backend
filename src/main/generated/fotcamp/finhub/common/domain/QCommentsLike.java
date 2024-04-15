package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentsLike is a Querydsl query type for CommentsLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentsLike extends EntityPathBase<CommentsLike> {

    private static final long serialVersionUID = -1169517580L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentsLike commentsLike = new QCommentsLike("commentsLike");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QComments comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public QCommentsLike(String variable) {
        this(CommentsLike.class, forVariable(variable), INITS);
    }

    public QCommentsLike(Path<? extends CommentsLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentsLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentsLike(PathMetadata metadata, PathInits inits) {
        this(CommentsLike.class, metadata, inits);
    }

    public QCommentsLike(Class<? extends CommentsLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new QComments(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

