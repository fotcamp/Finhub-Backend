package fotcamp.finhub.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private static Pageable createPageable(Integer page, Integer size, Sort sort) {
        int pageNumber = (page != null && page >= 1) ? page - 1 : DEFAULT_PAGE_NUMBER - 1;
        int pageSize = (size != null && size > 0) ? size : DEFAULT_PAGE_SIZE;

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private static Pageable createPageableNoSort(Integer page, Integer size) {
        int pageNumber = (page != null && page >= 1) ? page - 1 : DEFAULT_PAGE_NUMBER - 1;
        int pageSize = (size != null && size > 0) ? size : DEFAULT_PAGE_SIZE;

        return PageRequest.of(pageNumber, pageSize);
    }

    public static Pageable createPageableWithDefaultSort(Integer page, Integer size, String sortProperty) {
        return createPageable(page, size, Sort.by(sortProperty).descending());
    }

    public static Pageable createPageableWithDefaultSort(Integer page, Integer size, String sortProperty, String orderBy) {
        if ("asc".equals(orderBy)) {
            return createPageable(page, size, Sort.by(sortProperty).ascending());
        } else {
            return createPageable(page, size, Sort.by(sortProperty).descending());
        }
    }

    public static Pageable createPageableWithNoSort(Integer page, Integer size) {
        return createPageableNoSort(page, size);
    }
}
