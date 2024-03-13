package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.domain.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserTypeRepositoryCustom {
    // 기존 메서드
    List<UserType> searchAllUserTypeFilterList(String useYN);

    // 페이징 처리를 위한 메서드 추가
    Page<UserType> searchAllUserTypeFilterList(Pageable pageable, String useYN);
}
