package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuitReasons is a Querydsl query type for QuitReasons
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuitReasons extends EntityPathBase<QuitReasons> {

    private static final long serialVersionUID = -667882505L;

    public static final QQuitReasons quitReasons = new QQuitReasons("quitReasons");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reason = createString("reason");

    public final StringPath useYn = createString("useYn");

    public QQuitReasons(String variable) {
        super(QuitReasons.class, forVariable(variable));
    }

    public QQuitReasons(Path<? extends QuitReasons> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuitReasons(PathMetadata metadata) {
        super(QuitReasons.class, metadata);
    }

}

