package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGpt is a Querydsl query type for Gpt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGpt extends EntityPathBase<Gpt> {

    private static final long serialVersionUID = 495926850L;

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

    public final NumberPath<Long> topicId = createNumber("topicId", Long.class);

    public final NumberPath<Long> userTypeId = createNumber("userTypeId", Long.class);

    public QGpt(String variable) {
        super(Gpt.class, forVariable(variable));
    }

    public QGpt(Path<? extends Gpt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGpt(PathMetadata metadata) {
        super(Gpt.class, metadata);
    }

}

