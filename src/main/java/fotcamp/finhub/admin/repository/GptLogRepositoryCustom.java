package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.GptLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GptLogRepositoryCustom {
    // 기존 메서드
    List<GptLog> searchAllGptLogFilterList(Long topicId, Long usertypeId);

    // 페이징 처리를 위한 메서드 추가
    Page<GptLog> searchAllGptLogFilterList(Pageable pageable, Long topicId, Long usertypeId);
}
