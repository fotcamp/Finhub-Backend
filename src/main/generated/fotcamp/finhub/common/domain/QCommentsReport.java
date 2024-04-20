package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentsReport is a Querydsl query type for CommentsReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentsReport extends EntityPathBase<CommentsReport> {

    private static final long serialVersionUID = 1543280209L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentsReport commentsReport = new QCommentsReport("commentsReport");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QComments comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public QCommentsReport(String variable) {
        this(CommentsReport.class, forVariable(variable), INITS);
    }

    public QCommentsReport(Path<? extends CommentsReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentsReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentsReport(PathMetadata metadata, PathInits inits) {
        this(CommentsReport.class, metadata, inits);
    }

    public QCommentsReport(Class<? extends CommentsReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new QComments(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

