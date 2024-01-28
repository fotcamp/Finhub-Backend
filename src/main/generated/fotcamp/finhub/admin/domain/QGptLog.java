package fotcamp.finhub.admin.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGptLog is a Querydsl query type for GptLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGptLog extends EntityPathBase<GptLog> {

    private static final long serialVersionUID = 897283394L;

    public static final QGptLog gptLog = new QGptLog("gptLog");

    public final fotcamp.finhub.common.domain.QBaseEntity _super = new fotcamp.finhub.common.domain.QBaseEntity(this);

    public final StringPath answer = createString("answer");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath question = createString("question");

    public final NumberPath<Long> topicId = createNumber("topicId", Long.class);

    public final NumberPath<Long> usertypeId = createNumber("usertypeId", Long.class);

    public QGptLog(String variable) {
        super(GptLog.class, forVariable(variable));
    }

    public QGptLog(Path<? extends GptLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGptLog(PathMetadata metadata) {
        super(GptLog.class, metadata);
    }

}

