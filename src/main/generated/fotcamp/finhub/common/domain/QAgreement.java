package fotcamp.finhub.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAgreement is a Querydsl query type for Agreement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAgreement extends EntityPathBase<Agreement> {

    private static final long serialVersionUID = 63844961L;

    public static final QAgreement agreement = new QAgreement("agreement");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final BooleanPath privacy_policy = createBoolean("privacy_policy");

    public final BooleanPath terms_of_service = createBoolean("terms_of_service");

    public QAgreement(String variable) {
        super(Agreement.class, forVariable(variable));
    }

    public QAgreement(Path<? extends Agreement> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAgreement(PathMetadata metadata) {
        super(Agreement.class, metadata);
    }

}

