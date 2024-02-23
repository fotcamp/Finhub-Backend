package fotcamp.finhub.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String refreshToken;
    @NotBlank
    private String accountEmail;

    public RefreshToken(String refreshToken, String accountEmail) {
        this.refreshToken = refreshToken;
        this.accountEmail = accountEmail;
    }

    public RefreshToken updateToken(String token){
        this.refreshToken = token;
        return this;
    }
}
