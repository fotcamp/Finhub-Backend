package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.UserType;

import java.util.List;

public interface UserTypeRepositoryCustom {
    List<UserType> searchAllUserTypeFilterList(String useYN);
}
