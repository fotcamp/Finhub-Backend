package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryRepositoryCustom {
    // 기존 메서드
    List<Category> searchAllCategoryFilterList(String useYN);

    // 페이징 처리를 위한 메서드 추가
    Page<Category> searchAllCategoryFilterList(Pageable pageable, String useYN);
}
