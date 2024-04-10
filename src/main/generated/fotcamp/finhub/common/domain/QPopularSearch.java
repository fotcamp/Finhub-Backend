package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPopularSearch is a Querydsl query type for PopularSearch
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPopularSearch extends EntityPathBase<PopularSearch> {

    private static final long serialVersionUID = 890100408L;

    public static final QPopularSearch popularSearch = new QPopularSearch("popularSearch");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> frequency = createNumber("frequency", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public QPopularSearch(String variable) {
        super(PopularSearch.class, forVariable(variable));
    }

    public QPopularSearch(Path<? extends PopularSearch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPopularSearch(PathMetadata metadata) {
        super(PopularSearch.class, metadata);
    }

}

