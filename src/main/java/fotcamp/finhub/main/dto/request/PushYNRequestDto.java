package fotcamp.finhub.main.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PushYNRequestDto {

    private boolean yn;

    @JsonProperty("yn")
    public boolean isYn() {
        return yn;
    }

    public PushYNRequestDto(boolean yn) {
        this.yn = yn;
    }
}
