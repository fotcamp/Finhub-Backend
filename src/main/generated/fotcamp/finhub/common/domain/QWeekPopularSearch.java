package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWeekPopularSearch is a Querydsl query type for WeekPopularSearch
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWeekPopularSearch extends EntityPathBase<WeekPopularSearch> {

    private static final long serialVersionUID = 914076580L;

    public static final QWeekPopularSearch weekPopularSearch = new QWeekPopularSearch("weekPopularSearch");

    public final DatePath<java.time.LocalDate> analysisDate = createDate("analysisDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public final StringPath trend = createString("trend");

    public QWeekPopularSearch(String variable) {
        super(WeekPopularSearch.class, forVariable(variable));
    }

    public QWeekPopularSearch(Path<? extends WeekPopularSearch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWeekPopularSearch(PathMetadata metadata) {
        super(WeekPopularSearch.class, metadata);
    }

}

