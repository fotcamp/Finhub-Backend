package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.common.dto.process.PageInfoProcessDto;
import fotcamp.finhub.main.dto.process.AnnouncementProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AnnouncementResponseDto {

    private List<AnnouncementProcessDto> info;
    private PageInfoProcessDto pageInfoProcessDto;

    public AnnouncementResponseDto(List<AnnouncementProcessDto> info) {
        this.info = info;
    }

    public AnnouncementResponseDto(List<AnnouncementProcessDto> info, PageInfoProcessDto pageInfoProcessDto) {
        this.info = info;
        this.pageInfoProcessDto = pageInfoProcessDto;
    }
}
