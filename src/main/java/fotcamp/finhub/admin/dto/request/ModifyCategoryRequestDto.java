package fotcamp.finhub.admin.dto.request;

import fotcamp.finhub.admin.dto.process.ModifyTopicCategoryProcessDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModifyCategoryRequestDto(@NotNull Long id, @NotBlank String name, String useYN,
                                       String s3ImgUrl,
                                       @Valid List<ModifyTopicCategoryProcessDto> topicList) {
}
