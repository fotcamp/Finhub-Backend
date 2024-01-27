package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Topic;

import java.util.List;

public interface TopicRepositoryCustom {
    List<Topic> searchAllTopicFilterList(Long id, String useYN);

}
