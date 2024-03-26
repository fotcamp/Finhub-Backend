package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCalendarEmoticon is a Querydsl query type for CalendarEmoticon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCalendarEmoticon extends EntityPathBase<CalendarEmoticon> {

    private static final long serialVersionUID = 757227437L;

    public static final QCalendarEmoticon calendarEmoticon = new QCalendarEmoticon("calendarEmoticon");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final StringPath emoticon_img_path = createString("emoticon_img_path");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public QCalendarEmoticon(String variable) {
        super(CalendarEmoticon.class, forVariable(variable));
    }

    public QCalendarEmoticon(Path<? extends CalendarEmoticon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCalendarEmoticon(PathMetadata metadata) {
        super(CalendarEmoticon.class, metadata);
    }

}

