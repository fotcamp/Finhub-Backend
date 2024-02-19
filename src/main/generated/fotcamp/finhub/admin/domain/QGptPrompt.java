package fotcamp.finhub.admin.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGptPrompt is a Querydsl query type for GptPrompt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGptPrompt extends EntityPathBase<GptPrompt> {

    private static final long serialVersionUID = -789225818L;

    public static final QGptPrompt gptPrompt = new QGptPrompt("gptPrompt");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.time.LocalDateTime> createdTime = createDateTime("createdTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath prompt = createString("prompt");

    public QGptPrompt(String variable) {
        super(GptPrompt.class, forVariable(variable));
    }

    public QGptPrompt(Path<? extends GptPrompt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGptPrompt(PathMetadata metadata) {
        super(GptPrompt.class, metadata);
    }

}

