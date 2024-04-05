package fotcamp.finhub.main.dto.process.secondTab;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UserTypeListProcessDto {

    private Long id;
    private String name;
    private String img_path;

    public UserTypeListProcessDto(Long id, String name, String img_path) {
        this.id = id;
        this.name = name;
        this.img_path = img_path;
    }
}
