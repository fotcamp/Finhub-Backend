package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.TopicRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AllTopicRequestProcessDto {
    private final Long id;
    private final String term;
    private final String requester;
    private final LocalDateTime requestedAt;
    private final LocalDateTime resolvedAt;

    public AllTopicRequestProcessDto(TopicRequest topicRequest) {
        this.id = topicRequest.getId();
        this.term = topicRequest.getTerm();
        this.requester = topicRequest.getRequester();
        this.requestedAt = topicRequest.getRequestedAt();
        this.resolvedAt = topicRequest.getResolvedAt();
    }
}
