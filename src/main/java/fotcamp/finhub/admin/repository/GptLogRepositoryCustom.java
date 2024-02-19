package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.GptLog;

import java.util.List;

public interface GptLogRepositoryCustom {
    List<GptLog> searchAllGptLogFilterList(Long topicId, Long usertypeId);
}
