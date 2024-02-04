package fotcamp.finhub.admin.dto.process;


import jakarta.validation.constraints.NotBlank;

public record ModifyTopicCategoryProcessDto(Long topicId, @NotBlank String title, Long categoryId) {
}
