package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTopicRequest is a Querydsl query type for TopicRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTopicRequest extends EntityPathBase<TopicRequest> {

    private static final long serialVersionUID = 374212265L;

    public static final QTopicRequest topicRequest = new QTopicRequest("topicRequest");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> requestedAt = createDateTime("requestedAt", java.time.LocalDateTime.class);

    public final StringPath requester = createString("requester");

    public final DateTimePath<java.time.LocalDateTime> resolvedAt = createDateTime("resolvedAt", java.time.LocalDateTime.class);

    public final StringPath term = createString("term");

    public QTopicRequest(String variable) {
        super(TopicRequest.class, forVariable(variable));
    }

    public QTopicRequest(Path<? extends TopicRequest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTopicRequest(PathMetadata metadata) {
        super(TopicRequest.class, metadata);
    }

}

