package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecentSearch is a Querydsl query type for RecentSearch
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecentSearch extends EntityPathBase<RecentSearch> {

    private static final long serialVersionUID = -620309460L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecentSearch recentSearch = new QRecentSearch("recentSearch");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public final DateTimePath<java.time.LocalDateTime> localDateTime = createDateTime("localDateTime", java.time.LocalDateTime.class);

    public final QMember member;

    public QRecentSearch(String variable) {
        this(RecentSearch.class, forVariable(variable), INITS);
    }

    public QRecentSearch(Path<? extends RecentSearch> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecentSearch(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecentSearch(PathMetadata metadata, PathInits inits) {
        this(RecentSearch.class, metadata, inits);
    }

    public QRecentSearch(Class<? extends RecentSearch> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

