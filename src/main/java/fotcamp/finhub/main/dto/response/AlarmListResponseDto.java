package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.AlarmDetailProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlarmListResponseDto {

    private List<AlarmDetailProcessDto> alarmList;

    public AlarmListResponseDto(List<AlarmDetailProcessDto> alarmList) {
        this.alarmList = alarmList;
    }
}
