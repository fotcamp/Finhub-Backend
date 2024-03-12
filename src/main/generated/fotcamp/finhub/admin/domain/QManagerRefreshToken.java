package fotcamp.finhub.admin.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QManagerRefreshToken is a Querydsl query type for ManagerRefreshToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManagerRefreshToken extends EntityPathBase<ManagerRefreshToken> {

    private static final long serialVersionUID = -145372126L;

    public static final QManagerRefreshToken managerRefreshToken = new QManagerRefreshToken("managerRefreshToken");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    public QManagerRefreshToken(String variable) {
        super(ManagerRefreshToken.class, forVariable(variable));
    }

    public QManagerRefreshToken(Path<? extends ManagerRefreshToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QManagerRefreshToken(PathMetadata metadata) {
        super(ManagerRefreshToken.class, metadata);
    }

}

