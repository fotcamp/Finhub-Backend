package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryListProcessDto {

    private Long categoryId;
    private String name;

    public CategoryListProcessDto(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
