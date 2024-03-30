package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGptColumn is a Querydsl query type for GptColumn
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGptColumn extends EntityPathBase<GptColumn> {

    private static final long serialVersionUID = 1420280888L;

    public static final QGptColumn gptColumn = new QGptColumn("gptColumn");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath backgroundUrl = createString("backgroundUrl");

    public final StringPath content = createString("content");

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath summary = createString("summary");

    public final StringPath title = createString("title");

    public final ListPath<TopicGptColumn, QTopicGptColumn> topicGptColumnList = this.<TopicGptColumn, QTopicGptColumn>createList("topicGptColumnList", TopicGptColumn.class, QTopicGptColumn.class, PathInits.DIRECT2);

    public final StringPath useYN = createString("useYN");

    public QGptColumn(String variable) {
        super(GptColumn.class, forVariable(variable));
    }

    public QGptColumn(Path<? extends GptColumn> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGptColumn(PathMetadata metadata) {
        super(GptColumn.class, metadata);
    }

}

