package fotcamp.finhub.admin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.admin.domain.GptLog;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static fotcamp.finhub.admin.domain.QGptLog.gptLog;

@RequiredArgsConstructor
public class GptLogRepositoryImpl implements GptLogRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GptLog> searchAllGptLogFilterList(Long topicId, Long usertypeId) {
        return queryFactory
                .selectFrom(gptLog)
                .where(topicEq(topicId), usertypeEq(usertypeId))
                .fetch();
    }

    private BooleanExpression topicEq(Long id) {
        if (id != null) {
            return gptLog.topicId.eq(id);
        }
        return null;
    }

    private BooleanExpression usertypeEq(Long id) {
        if (id != null) {
            return gptLog.usertypeId.eq(id);
        }
        return null;
    }
}
