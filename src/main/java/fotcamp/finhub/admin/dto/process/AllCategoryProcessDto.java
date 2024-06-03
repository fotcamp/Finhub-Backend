package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Category;
import fotcamp.finhub.common.service.AwsS3Service;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AllCategoryProcessDto {
    private final Long id;
    private final String name;
    private final String useYN;
    private final Long position;

    public AllCategoryProcessDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.useYN = category.getUseYN();
        this.position = category.getPosition();
    }
}
