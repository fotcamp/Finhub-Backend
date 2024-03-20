package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAnnounceMent is a Querydsl query type for AnnounceMent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnnounceMent extends EntityPathBase<AnnounceMent> {

    private static final long serialVersionUID = -1826591312L;

    public static final QAnnounceMent announceMent = new QAnnounceMent("announceMent");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath content = createString("content");

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath title = createString("title");

    public QAnnounceMent(String variable) {
        super(AnnounceMent.class, forVariable(variable));
    }

    public QAnnounceMent(Path<? extends AnnounceMent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnnounceMent(PathMetadata metadata) {
        super(AnnounceMent.class, metadata);
    }

}

