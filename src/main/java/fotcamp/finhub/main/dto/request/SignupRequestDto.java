package fotcamp.finhub.main.dto.request;


import fotcamp.finhub.main.domain.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    private String email;
    private String name;
    private String password;


    @Builder
    public static Member toEntity1(SignupRequestDto signupRequestDto, PasswordEncoder passwordEncoder){
        return Member.builder()
                .email(signupRequestDto.getEmail())
                .name(signupRequestDto.getName())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                //.password(pass signupRequestDto.getPassword())
                .build();
    }

}
