package fotcamp.finhub.admin.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.admin.domain.GptLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static fotcamp.finhub.admin.domain.QGptLog.gptLog;
import static fotcamp.finhub.common.domain.QCategory.category;

@RequiredArgsConstructor
public class GptLogRepositoryImpl implements GptLogRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    // 기존 메서드
    @Override
    public List<GptLog> searchAllGptLogFilterList(Long topicId, Long usertypeId) {
        return queryFactory
                .selectFrom(gptLog)
                .where(topicEq(topicId), usertypeEq(usertypeId))
                .fetch();
    }

    // 페이징 처리를 위한 메서드 구현
    @Override
    public Page<GptLog> searchAllGptLogFilterList(Pageable pageable, Long topicId, Long usertypeId) {
        // 데이터 목록 조회
        List<GptLog> gptLogs = queryFactory
                .selectFrom(gptLog)
                .where(topicEq(topicId), usertypeEq(usertypeId))
                .orderBy(getOrderSpecifiers(pageable.getSort())) // 정렬 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .stream().toList();

        long total = queryFactory
                .selectFrom(gptLog)
                .where(topicEq(topicId), usertypeEq(usertypeId))
                .stream().count();

        return new PageImpl<>(gptLogs, pageable, total);
    }

    // Pageable의 Sort 객체를 이용해 Querydsl의 OrderSpecifier 배열로 변환
    private com.querydsl.core.types.OrderSpecifier<?>[] getOrderSpecifiers(org.springframework.data.domain.Sort sort) {
        return sort.stream()
                .map(order -> {
                    PathBuilder<GptLog> entityPath = new PathBuilder<>(GptLog.class, "gptLog");
                    Expression<?> expression = entityPath.get(order.getProperty());
                    return new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, expression);
                })
                .toArray(OrderSpecifier[]::new);
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
