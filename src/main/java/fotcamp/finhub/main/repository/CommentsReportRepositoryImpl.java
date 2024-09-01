package fotcamp.finhub.main.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.common.domain.CommentsReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

import static fotcamp.finhub.common.domain.QCommentsReport.commentsReport;

@RequiredArgsConstructor
public class CommentsReportRepositoryImpl implements CommentsReportRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentsReport> searchAllTCommentsReportFilterList(Pageable pageable, String useYn, String isProcessed) {
        List<CommentsReport> commentsReports = queryFactory
                .selectFrom(commentsReport)
                .where(Expressions.asBoolean(true).isTrue()
                        .and(useYNEq(useYn))
                        .and(isProcessedEq(isProcessed)))
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .stream().toList();

        long total = queryFactory
                .selectFrom(commentsReport)
                .where(Expressions.asBoolean(true).isTrue()
                        .and(useYNEq(useYn))
                        .and(isProcessedEq(isProcessed)))
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
            return commentsReport.reportedComment.useYn.eq("Y");
        } else if ("N".equals(useYn)) {
            return commentsReport.reportedComment.useYn.eq("N");
        } else {
            return null;
        }
    }

    private BooleanExpression isProcessedEq(String isProcessed) {
        if ("Y".equals(isProcessed)) {
            return commentsReport.isProcessed.eq("Y");
        } else if ("N".equals(isProcessed)) {
            return commentsReport.isProcessed.eq("N");
        } else {
            return null;
        }
    }
}
