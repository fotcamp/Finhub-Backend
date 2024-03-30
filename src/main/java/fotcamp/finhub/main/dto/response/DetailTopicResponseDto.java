package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.DetailNextTopicProcessDto;
import fotcamp.finhub.main.dto.process.DetailTopicProcessDto;
import fotcamp.finhub.main.dto.process.JobListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DetailTopicResponseDto {

    private boolean isScrapped;
    private DetailTopicProcessDto topicInfo;
    private List<JobListProcessDto> jobLists;
    private String content;
    private DetailNextTopicProcessDto nextTopic;

    public DetailTopicResponseDto(DetailTopicProcessDto topicInfo, DetailNextTopicProcessDto nextTopic, boolean isScrapped, List<JobListProcessDto> jobLists, String content) {
        this.topicInfo = topicInfo;
        this.nextTopic = nextTopic;
        this.isScrapped = isScrapped;
        this.jobLists = jobLists;
        this.content = content;
    }
}
