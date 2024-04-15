package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.AnnouncementProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AnnouncementResponseDto {

    private List<AnnouncementProcessDto> info;

    public AnnouncementResponseDto(List<AnnouncementProcessDto> info) {
        this.info = info;
    }
}
