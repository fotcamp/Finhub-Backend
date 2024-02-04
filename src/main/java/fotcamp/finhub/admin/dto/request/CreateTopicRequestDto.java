package fotcamp.finhub.admin.dto.request;


public record CreateTopicRequestDto(Long categoryId, String title, String definition, String shortDefinition,
                                    String thumbnail, String useYN, String createdBy) {
}
