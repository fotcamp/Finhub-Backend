package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuitMember is a Querydsl query type for QuitMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuitMember extends EntityPathBase<QuitMember> {

    private static final long serialVersionUID = 528387154L;

    public static final QQuitMember quitMember = new QQuitMember("quitMember");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath age = createString("age");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public QQuitMember(String variable) {
        super(QuitMember.class, forVariable(variable));
    }

    public QQuitMember(Path<? extends QuitMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuitMember(PathMetadata metadata) {
        super(QuitMember.class, metadata);
    }

}

