package fotcamp.finhub.main.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.common.domain.CommentsReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static fotcamp.finhub.common.domain.QCommentsReport.commentsReport;

@RequiredArgsConstructor
public class CommentsReportRepositoryImpl implements CommentsReportRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentsReport> searchAllTCommentsReportFilterList(Pageable pageable, String useYn) {
        // 데이터 목록 조회
        List<CommentsReport> commentsReports = queryFactory
                .selectFrom(commentsReport)
                .where(useYNEq(useYn))
                .orderBy(getOrderSpecifiers(pageable.getSort())) // 정렬 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .stream().toList();

        long total = queryFactory
                .selectFrom(commentsReport)
                .where(useYNEq(useYn))
                .stream().count();

        return new PageImpl<>(commentsReports, pageable, total);
    }

    // Pageable의 Sort 객체를 이용해 Querydsl의 OrderSpecifier 배열로 변환
    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    PathBuilder<CommentsReport> entityPath = new PathBuilder<>(CommentsReport.class, "commentsReport");
                    Expression<?> expression = entityPath.get(order.getProperty());
                    return new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, expression);
                })
                .toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression useYNEq(String useYn) {
        if ("Y".equals(useYn)) {
            return commentsReport.useYn.eq("Y");
        } else if ("N".equals(useYn)) {
            return commentsReport.useYn.eq("N");
        } else {
            // resolvedYN이 "Y" 또는 "N" 이외의 값을 가질 경우 처리
            // 이 경우 모든 데이터를 반환하거나, 어떠한 데이터도 반환하지 않는 등의 처리가 필요할 수 있습니다.
            // 아래 코드는 예시로 모든 데이터를 반환하도록 합니다. 상황에 맞게 적절한 처리를 선택하세요.
            return null; // 또는 다른 적절한 BooleanExpression 반환
        }
    }
}
