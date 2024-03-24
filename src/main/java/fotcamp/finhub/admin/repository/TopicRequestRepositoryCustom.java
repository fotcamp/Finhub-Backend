package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.TopicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicRequestRepositoryCustom {
    // 페이징 처리를 위한 메서드 추가
    Page<TopicRequest> searchAllTopicRequestFilterList(Pageable pageable, String resolvedY);
}
