package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.AllCategoryProcessDto;
import fotcamp.finhub.common.dto.process.PageInfoProcessDto;

import java.util.List;

public record AllCategoryResponseDto(List<AllCategoryProcessDto> categoryList) {
}
