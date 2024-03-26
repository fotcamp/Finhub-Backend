package fotcamp.finhub.common.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TopicRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String term;
    private String requester;
    private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;

    public void setResolvedAt(LocalDateTime now) {
        resolvedAt = now;
    }

    public TopicRequest(String term, String requester, LocalDateTime requestedAt) {
        this.term = term;
        this.requester = requester;
        this.requestedAt = requestedAt;
    }
}
