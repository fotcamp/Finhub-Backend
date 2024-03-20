package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberScrap is a Querydsl query type for MemberScrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberScrap extends EntityPathBase<MemberScrap> {

    private static final long serialVersionUID = 1169982446L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberScrap memberScrap = new QMemberScrap("memberScrap");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final QTopic topic;

    public QMemberScrap(String variable) {
        this(MemberScrap.class, forVariable(variable), INITS);
    }

    public QMemberScrap(Path<? extends MemberScrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberScrap(PathMetadata metadata, PathInits inits) {
        this(MemberScrap.class, metadata, inits);
    }

    public QMemberScrap(Class<? extends MemberScrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.topic = inits.isInitialized("topic") ? new QTopic(forProperty("topic"), inits.get("topic")) : null;
    }

}

