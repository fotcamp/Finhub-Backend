package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuiz is a Querydsl query type for Quiz
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuiz extends EntityPathBase<Quiz> {

    private static final long serialVersionUID = -1805834338L;

    public static final QQuiz quiz = new QQuiz("quiz");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath answer = createString("answer");

    public final StringPath comment = createString("comment");

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath question = createString("question");

    public final DatePath<java.time.LocalDate> targetDate = createDate("targetDate", java.time.LocalDate.class);

    public final ListPath<TopicQuiz, QTopicQuiz> topicList = this.<TopicQuiz, QTopicQuiz>createList("topicList", TopicQuiz.class, QTopicQuiz.class, PathInits.DIRECT2);

    public QQuiz(String variable) {
        super(Quiz.class, forVariable(variable));
    }

    public QQuiz(Path<? extends Quiz> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuiz(PathMetadata metadata) {
        super(Quiz.class, metadata);
    }

}

