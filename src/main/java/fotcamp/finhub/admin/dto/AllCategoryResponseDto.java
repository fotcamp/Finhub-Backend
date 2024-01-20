package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Category;
import lombok.Data;

@Data
public class AllCategoryResponseDto {
    private Long id;
    private String name;
    private String useYN;

    public AllCategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.useYN = category.getUseYN();
    }
}
