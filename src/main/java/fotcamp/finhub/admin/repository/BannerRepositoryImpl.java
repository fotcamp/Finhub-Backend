package fotcamp.finhub.admin.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.common.domain.Banner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;

import static fotcamp.finhub.common.domain.QBanner.banner;

@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    // 페이징 처리를 위한 메서드 구현
    @Override
    public Page<Banner> searchAllBannerFilterList(Pageable pageable, String useYN) {
        List<Banner> userTypes = queryFactory
                .selectFrom(banner)
                .where(useYNEq(useYN))
                .orderBy(getOrderSpecifiers(pageable.getSort())) // 정렬 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .stream().toList();

        long total = queryFactory
                .selectFrom(banner)
                .where(useYNEq(useYN))
                .stream().count();

        return new PageImpl<>(userTypes, pageable, total);
    }

    // Pageable의 Sort 객체를 이용해 Querydsl의 OrderSpecifier 배열로 변환
    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    PathBuilder<Banner> entityPath = new PathBuilder<>(Banner.class, "banner");
                    Expression<?> expression = entityPath.get(order.getProperty());
                    return new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, expression);
                })
                .toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression useYNEq(String useYN) {
        return StringUtils.hasText(useYN) ? banner.useYN.eq(useYN) : null;
    }
}
