package fotcamp.finhub.admin.dto.request;

import java.util.List;

public record SaveGptColumnRequestDto(String title, String summary, String content, String backgroundUrl, String createdBy, String useYN,
                                      List<Long> topicList) {
}
