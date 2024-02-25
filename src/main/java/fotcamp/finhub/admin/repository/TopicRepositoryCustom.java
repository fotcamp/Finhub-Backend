package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Category;
import fotcamp.finhub.common.domain.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicRepositoryCustom {
    // 기존 메서드
    List<Topic> searchAllTopicFilterList(Long id, String useYN);

    // 페이징 처리를 위한 메서드 추가
    Page<Topic> searchAllTopicFilterList(Pageable pageable, Long id, String useYN);
}
