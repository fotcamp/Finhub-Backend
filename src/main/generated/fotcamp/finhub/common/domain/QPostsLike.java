package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostsLike is a Querydsl query type for PostsLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostsLike extends EntityPathBase<PostsLike> {

    private static final long serialVersionUID = 1108303457L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostsLike postsLike = new QPostsLike("postsLike");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final QGptColumn gptColumn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public QPostsLike(String variable) {
        this(PostsLike.class, forVariable(variable), INITS);
    }

    public QPostsLike(Path<? extends PostsLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostsLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostsLike(PathMetadata metadata, PathInits inits) {
        this(PostsLike.class, metadata, inits);
    }

    public QPostsLike(Class<? extends PostsLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gptColumn = inits.isInitialized("gptColumn") ? new QGptColumn(forProperty("gptColumn")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

