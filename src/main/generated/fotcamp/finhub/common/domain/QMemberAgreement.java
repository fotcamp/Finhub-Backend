package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAgreement is a Querydsl query type for MemberAgreement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAgreement extends EntityPathBase<MemberAgreement> {

    private static final long serialVersionUID = -2080557145L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAgreement memberAgreement = new QMemberAgreement("memberAgreement");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final BooleanPath privacyPolicy = createBoolean("privacyPolicy");

    public final DateTimePath<java.time.LocalDateTime> pushUpdateTime = createDateTime("pushUpdateTime", java.time.LocalDateTime.class);

    public final BooleanPath pushYn = createBoolean("pushYn");

    public final BooleanPath termsOfService = createBoolean("termsOfService");

    public QMemberAgreement(String variable) {
        this(MemberAgreement.class, forVariable(variable), INITS);
    }

    public QMemberAgreement(Path<? extends MemberAgreement> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAgreement(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAgreement(PathMetadata metadata, PathInits inits) {
        this(MemberAgreement.class, metadata, inits);
    }

    public QMemberAgreement(Class<? extends MemberAgreement> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

