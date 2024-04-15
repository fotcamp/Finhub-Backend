package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostsScrap is a Querydsl query type for PostsScrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostsScrap extends EntityPathBase<PostsScrap> {

    private static final long serialVersionUID = 3961415L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostsScrap postsScrap = new QPostsScrap("postsScrap");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final QGptColumn gptColumn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public QPostsScrap(String variable) {
        this(PostsScrap.class, forVariable(variable), INITS);
    }

    public QPostsScrap(Path<? extends PostsScrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostsScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostsScrap(PathMetadata metadata, PathInits inits) {
        this(PostsScrap.class, metadata, inits);
    }

    public QPostsScrap(Class<? extends PostsScrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gptColumn = inits.isInitialized("gptColumn") ? new QGptColumn(forProperty("gptColumn")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

