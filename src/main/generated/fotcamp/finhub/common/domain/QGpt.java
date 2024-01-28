package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGpt is a Querydsl query type for Gpt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGpt extends EntityPathBase<Gpt> {

    private static final long serialVersionUID = 495926850L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGpt gpt = new QGpt("gpt");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final StringPath content = createString("content");

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final QTopic topic;

    public final QUserType userType;

    public final StringPath useYN = createString("useYN");

    public QGpt(String variable) {
        this(Gpt.class, forVariable(variable), INITS);
    }

    public QGpt(Path<? extends Gpt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGpt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGpt(PathMetadata metadata, PathInits inits) {
        this(Gpt.class, metadata, inits);
    }

    public QGpt(Class<? extends Gpt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.topic = inits.isInitialized("topic") ? new QTopic(forProperty("topic"), inits.get("topic")) : null;
        this.userType = inits.isInitialized("userType") ? new QUserType(forProperty("userType")) : null;
    }

}

