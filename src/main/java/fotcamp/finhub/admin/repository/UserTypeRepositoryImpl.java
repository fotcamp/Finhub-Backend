package fotcamp.finhub.admin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fotcamp.finhub.common.domain.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static fotcamp.finhub.common.domain.QUserType.userType;


@RequiredArgsConstructor
public class UserTypeRepositoryImpl implements UserTypeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserType> searchAllUserTypeFilterList(String useYN) {
        return queryFactory
                .selectFrom(userType)
                .where(useYNEq(useYN))
                .fetch();
    }

    private BooleanExpression useYNEq(String useYN) {
        return StringUtils.hasText(useYN) ? userType.useYN.eq(useYN) : null;
    }
}
