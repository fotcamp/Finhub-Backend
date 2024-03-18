package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserType is a Querydsl query type for UserType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserType extends EntityPathBase<UserType> {

    private static final long serialVersionUID = 2059422318L;

    public static final QUserType userType = new QUserType("userType");

    public final StringPath avatarImgPath = createString("avatarImgPath");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Member, QMember> memberList = this.<Member, QMember>createList("memberList", Member.class, QMember.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath useYN = createString("useYN");

    public QUserType(String variable) {
        super(UserType.class, forVariable(variable));
    }

    public QUserType(Path<? extends UserType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserType(PathMetadata metadata) {
        super(UserType.class, metadata);
    }

}

