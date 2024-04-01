package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JobListProcessDto {

    private Long id;
    private String name;

    public JobListProcessDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
