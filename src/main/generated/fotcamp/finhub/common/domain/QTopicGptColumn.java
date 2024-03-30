package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTopicGptColumn is a Querydsl query type for TopicGptColumn
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTopicGptColumn extends EntityPathBase<TopicGptColumn> {

    private static final long serialVersionUID = 479025531L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTopicGptColumn topicGptColumn = new QTopicGptColumn("topicGptColumn");

    public final QGptColumn gptColumn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTopic topic;

    public QTopicGptColumn(String variable) {
        this(TopicGptColumn.class, forVariable(variable), INITS);
    }

    public QTopicGptColumn(Path<? extends TopicGptColumn> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTopicGptColumn(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTopicGptColumn(PathMetadata metadata, PathInits inits) {
        this(TopicGptColumn.class, metadata, inits);
    }

    public QTopicGptColumn(Class<? extends TopicGptColumn> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gptColumn = inits.isInitialized("gptColumn") ? new QGptColumn(forProperty("gptColumn")) : null;
        this.topic = inits.isInitialized("topic") ? new QTopic(forProperty("topic"), inits.get("topic")) : null;
    }

}

