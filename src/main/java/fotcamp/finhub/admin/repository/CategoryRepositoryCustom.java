package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> searchAllCategoryFilterList(String useYN);
}
