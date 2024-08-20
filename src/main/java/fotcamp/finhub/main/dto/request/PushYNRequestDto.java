package fotcamp.finhub.main.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PushYNRequestDto {

    private boolean pushYn;

    @JsonProperty("yn")
    public boolean isPushYn() {
        return pushYn;
    }

    public PushYNRequestDto(boolean pushYn) {
        this.pushYn = pushYn;
    }
}
