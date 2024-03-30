package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.JobListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SettingJobResponseDto {

    private String defaultJob;
    private List<JobListProcessDto> jobs;

    public SettingJobResponseDto(String defaultJob, List<JobListProcessDto> jobs) {
        this.defaultJob = defaultJob;
        this.jobs = jobs;
    }
}
