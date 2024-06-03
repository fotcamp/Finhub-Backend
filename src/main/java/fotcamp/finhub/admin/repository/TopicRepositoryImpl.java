package fotcamp.finhub.admin.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.common.domain.Category;
import fotcamp.finhub.common.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;

import static fotcamp.finhub.common.domain.QCategory.category;
import static fotcamp.finhub.common.domain.QTopic.topic;

@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    // 기존 메서드
    @Override
    public List<Topic> searchAllTopicFilterList(Long id, String useYN) {
         return queryFactory
                .selectFrom(topic)
                .join(topic.category, category).fetchJoin()
                .where(categoryEq(id), useYNEq(useYN))
                .fetch();
    }

    // 페이징 처리를 위한 메서드 구현
    @Override
    public Page<Topic> searchAllTopicFilterList(Pageable pageable, Long id, String useYN) {
        // 데이터 목록 조회
        List<Topic> topics = queryFactory
                .selectFrom(topic)
                .join(topic.category, category).fetchJoin()
                .where(categoryEq(id), useYNEq(useYN))
                .orderBy(getOrderSpecifiers(pageable.getSort())) // 정렬 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .stream().toList();

        long total = queryFactory
                .selectFrom(topic)
                .join(topic.category, category).fetchJoin()
                .where(categoryEq(id), useYNEq(useYN))
                .stream().count();

        return new PageImpl<>(topics, pageable, total);
    }

    // Pageable의 Sort 객체를 이용해 Querydsl의 OrderSpecifier 배열로 변환
    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    PathBuilder<Topic> entityPath = new PathBuilder<>(Topic.class, "topic");
                    Expression<?> expression = entityPath.get(order.getProperty());
                    OrderSpecifier<?> orderSpecifier = order.isAscending() ? new OrderSpecifier(Order.ASC, expression)
                            : new OrderSpecifier(Order.DESC, expression);
                    // NULL 값이 마지막에 오도록 설정
                    return orderSpecifier.nullsLast();
                })
                .toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression categoryEq(Long id) {
        if (id != null) {
            return topic.category.id.eq(id);
        }
        return null;
    }

    private BooleanExpression useYNEq(String useYN) {
        if (StringUtils.hasText(useYN)) {
            if ("Y".equals(useYN) || "N".equals(useYN)) {
                return topic.useYN.eq(useYN);
            } else {
                // 유효하지 않은 값이 입력될 경우 예외 발생 또는 다른 처리
                throw new IllegalArgumentException("Invalid useYN value: " + useYN);
            }
        }
        return null; // useYN이 null이거나 비어있는 경우 모든 카테고리를 반환
    }
}
