package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFeedback is a Querydsl query type for Feedback
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedback extends EntityPathBase<Feedback> {

    private static final long serialVersionUID = 2134385742L;

    public static final QFeedback feedback1 = new QFeedback("feedback1");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath adminResponse = createString("adminResponse");

    public final StringPath appVersion = createString("appVersion");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final StringPath email = createString("email");

    public final StringPath feedback = createString("feedback");

    public final StringPath fileUrl1 = createString("fileUrl1");

    public final StringPath fileUrl2 = createString("fileUrl2");

    public final StringPath fileUrl3 = createString("fileUrl3");

    public final StringPath fileUrl4 = createString("fileUrl4");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath reply = createString("reply");

    public final StringPath userAgent = createString("userAgent");

    public QFeedback(String variable) {
        super(Feedback.class, forVariable(variable));
    }

    public QFeedback(Path<? extends Feedback> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFeedback(PathMetadata metadata) {
        super(Feedback.class, metadata);
    }

}

