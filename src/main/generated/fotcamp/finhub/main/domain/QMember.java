package fotcamp.finhub.main.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1696458097L;

    public static final QMember member = new QMember("member1");

    public final StringPath email = createString("email");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<fotcamp.finhub.common.domain.MemberQuiz, fotcamp.finhub.common.domain.QMemberQuiz> quizList = this.<fotcamp.finhub.common.domain.MemberQuiz, fotcamp.finhub.common.domain.QMemberQuiz>createList("quizList", fotcamp.finhub.common.domain.MemberQuiz.class, fotcamp.finhub.common.domain.QMemberQuiz.class, PathInits.DIRECT2);

    public final EnumPath<RoleType> role = createEnum("role", RoleType.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

