package fotcamp.finhub.admin.dto.request;

import fotcamp.finhub.admin.dto.process.ModifyTopicCategoryProcessDto;

import java.util.List;

public record ModifyCategoryRequestDto(Long id, String name, String thumbnailImgPath, String useYN,
                                       List<ModifyTopicCategoryProcessDto> topicList) {
}
