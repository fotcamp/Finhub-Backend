package fotcamp.finhub.admin.dto;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {
    private String id;
    private String password;

    @Builder
    public LoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
