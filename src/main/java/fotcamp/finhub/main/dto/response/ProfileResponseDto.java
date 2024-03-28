package fotcamp.finhub.main.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {

    private String email;

    public ProfileResponseDto(String email) {
        this.email = email;
    }
}
