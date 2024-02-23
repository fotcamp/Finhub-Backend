package fotcamp.finhub.common.dto.request;


import fotcamp.finhub.common.domain.Member;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    private String email;
    private String name;
    private String password;

    @Builder
    public static Member toEntity(SignupRequestDto signupRequestDto){
        return Member.builder()
                .email(signupRequestDto.getEmail())
                .name(signupRequestDto.getName())
                .password(signupRequestDto.getPassword())
                .build();
    }
}
