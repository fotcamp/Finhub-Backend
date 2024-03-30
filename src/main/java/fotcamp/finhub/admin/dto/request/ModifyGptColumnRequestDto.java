package fotcamp.finhub.admin.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ModifyGptColumnRequestDto {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String backgroundUrl;
    private String useYN;
    private String createdBy;
    private List<Long> topicList; // 관련 토픽들
}
