package fotcamp.finhub.admin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteCalendarEmojiRequestDto {
    private Long id;

    public DeleteCalendarEmojiRequestDto(Long id) {
        this.id = id;
    }
}
