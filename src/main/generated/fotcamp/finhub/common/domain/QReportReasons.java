package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportReasons is a Querydsl query type for ReportReasons
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportReasons extends EntityPathBase<ReportReasons> {

    private static final long serialVersionUID = -1132768334L;

    public static final QReportReasons reportReasons = new QReportReasons("reportReasons");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath reason = createString("reason");

    public final StringPath useYn = createString("useYn");

    public QReportReasons(String variable) {
        super(ReportReasons.class, forVariable(variable));
    }

    public QReportReasons(Path<? extends ReportReasons> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportReasons(PathMetadata metadata) {
        super(ReportReasons.class, metadata);
    }

}

