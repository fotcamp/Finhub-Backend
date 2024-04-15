package fotcamp.finhub.main.dto.process;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
public class AnnouncementProcessDto {

    private Long id;
    private String title;
    private String content;
    private LocalDate time;

    public AnnouncementProcessDto(Long id, String title, String content, LocalDate time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
    }
}
