package fotcamp.finhub.admin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.common.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static fotcamp.finhub.common.domain.QCategory.category;
import static fotcamp.finhub.common.domain.QTopic.topic;

@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Topic> searchAllTopicFilterList(Long id, String useYN) {
         return queryFactory
                .selectFrom(topic)
                .join(topic.category, category).fetchJoin()
                .where(categoryEq(id), useYNEq(useYN))
                .fetch();
    }

    private BooleanExpression categoryEq(Long id) {
        if (id != null) {
            return topic.category.id.eq(id);
        }
        return null;
    }

    private BooleanExpression useYNEq(String useYN) {
        return StringUtils.hasText(useYN) ? topic.useYN.eq(useYN) : null;
    }
}
