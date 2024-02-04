package fotcamp.finhub.admin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.common.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static fotcamp.finhub.common.domain.QCategory.category;


@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Category> searchAllCategoryFilterList(String useYN) {
        return queryFactory
                .selectFrom(category)
                .where(useYNEq(useYN))
                .fetch();
    }

    private BooleanExpression useYNEq(String useYN) {
        return StringUtils.hasText(useYN) ? category.useYN.eq(useYN) : null;
    }
}
