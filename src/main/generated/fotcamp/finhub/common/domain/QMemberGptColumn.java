package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberGptColumn is a Querydsl query type for MemberGptColumn
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberGptColumn extends EntityPathBase<MemberGptColumn> {

    private static final long serialVersionUID = -724121218L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberGptColumn memberGptColumn = new QMemberGptColumn("memberGptColumn");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final QGptColumn gptColumn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public QMemberGptColumn(String variable) {
        this(MemberGptColumn.class, forVariable(variable), INITS);
    }

    public QMemberGptColumn(Path<? extends MemberGptColumn> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberGptColumn(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberGptColumn(PathMetadata metadata, PathInits inits) {
        this(MemberGptColumn.class, metadata, inits);
    }

    public QMemberGptColumn(Class<? extends MemberGptColumn> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gptColumn = inits.isInitialized("gptColumn") ? new QGptColumn(forProperty("gptColumn")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

