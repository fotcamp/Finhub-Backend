package fotcamp.finhub.admin.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.common.domain.TopicRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;

import static fotcamp.finhub.common.domain.QTopicRequest.topicRequest;

@RequiredArgsConstructor
public class TopicRequestRepositoryImpl implements TopicRequestRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    // 페이징 처리를 위한 메서드 구현
    @Override
    public Page<TopicRequest> searchAllTopicRequestFilterList(Pageable pageable, String resolvedYN) {
        // 데이터 목록 조회
        List<TopicRequest> topicRequests = queryFactory
                .selectFrom(topicRequest)
                .where(resolvedYNEq(resolvedYN))
                .orderBy(getOrderSpecifiers(pageable.getSort())) // 정렬 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .stream().toList();

        long total = queryFactory
                .selectFrom(topicRequest)
                .where(resolvedYNEq(resolvedYN))
                .stream().count();

        return new PageImpl<>(topicRequests, pageable, total);
    }

    // Pageable의 Sort 객체를 이용해 Querydsl의 OrderSpecifier 배열로 변환
    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    PathBuilder<TopicRequest> entityPath = new PathBuilder<>(TopicRequest.class, "topicRequest");
                    Expression<?> expression = entityPath.get(order.getProperty());
                    return new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, expression);
                })
                .toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression resolvedYNEq(String resolvedYN) {
        return StringUtils.hasText(resolvedYN) ? topicRequest.resolvedAt.isNotNull() : topicRequest.resolvedAt.isNull();
    }
}
