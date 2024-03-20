package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberQuiz is a Querydsl query type for MemberQuiz
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberQuiz extends EntityPathBase<MemberQuiz> {

    private static final long serialVersionUID = 453340824L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberQuiz memberQuiz = new QMemberQuiz("memberQuiz");

    public final StringPath answerYN = createString("answerYN");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final QQuiz quiz;

    public final DateTimePath<java.time.LocalDateTime> solvedTime = createDateTime("solvedTime", java.time.LocalDateTime.class);

    public QMemberQuiz(String variable) {
        this(MemberQuiz.class, forVariable(variable), INITS);
    }

    public QMemberQuiz(Path<? extends MemberQuiz> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberQuiz(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberQuiz(PathMetadata metadata, PathInits inits) {
        this(MemberQuiz.class, metadata, inits);
    }

    public QMemberQuiz(Class<? extends MemberQuiz> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.quiz = inits.isInitialized("quiz") ? new QQuiz(forProperty("quiz")) : null;
    }

}

