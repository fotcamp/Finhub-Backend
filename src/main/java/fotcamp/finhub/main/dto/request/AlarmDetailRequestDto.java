package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmDetailRequestDto {

    private Long id;

    public AlarmDetailRequestDto(Long id) {
        this.id = id;
    }
}
