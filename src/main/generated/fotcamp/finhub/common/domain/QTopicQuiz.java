package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTopicQuiz is a Querydsl query type for TopicQuiz
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTopicQuiz extends EntityPathBase<TopicQuiz> {

    private static final long serialVersionUID = -523627397L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTopicQuiz topicQuiz = new QTopicQuiz("topicQuiz");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QQuiz quiz;

    public final QTopic topic;

    public QTopicQuiz(String variable) {
        this(TopicQuiz.class, forVariable(variable), INITS);
    }

    public QTopicQuiz(Path<? extends TopicQuiz> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTopicQuiz(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTopicQuiz(PathMetadata metadata, PathInits inits) {
        this(TopicQuiz.class, metadata, inits);
    }

    public QTopicQuiz(Class<? extends TopicQuiz> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.quiz = inits.isInitialized("quiz") ? new QQuiz(forProperty("quiz")) : null;
        this.topic = inits.isInitialized("topic") ? new QTopic(forProperty("topic"), inits.get("topic")) : null;
    }

}

