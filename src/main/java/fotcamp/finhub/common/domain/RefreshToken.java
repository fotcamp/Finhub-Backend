package fotcamp.finhub.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

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

