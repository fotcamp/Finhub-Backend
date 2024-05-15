package fotcamp.finhub.main.dto.process;


import fotcamp.finhub.common.domain.MemberNotification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
public class AlarmDetailProcessDto {


    private Long id;
    private String title;
    private String message;
    private String url;
    private LocalDateTime sentAt;
    private LocalDateTime receivedAt;

    public AlarmDetailProcessDto(Long id, String title, String message, String url, LocalDateTime sentAt, LocalDateTime receivedAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.url = url;
        this.sentAt = sentAt;
        this.receivedAt = receivedAt;
    }

}
