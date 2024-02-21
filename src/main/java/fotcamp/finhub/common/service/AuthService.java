package fotcamp.finhub.common.service;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.dto.CustomUserInfoDto;
import fotcamp.finhub.common.dto.LoginRequestDto;
import fotcamp.finhub.common.repository.MemberRepository;
import fotcamp.finhub.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<ApiResponseWrapper> login(LoginRequestDto loginRequestDto){
        try{
            String email = loginRequestDto.getEmail();
            String password = loginRequestDto.getPassword();
            Member member = memberRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

            if(!passwordEncoder.matches(password,member.getPassword())){
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }
            System.out.println("repository find member"+ member.getMemberId());
            CustomUserInfoDto info = modelMapper.map(member, CustomUserInfoDto.class);
            System.out.println("authservice:"+info.getMemberId());
            System.out.println(info.getEmail());
            String accessToken = jwtUtil.createAccessToken(info);
            return ResponseEntity.ok(ApiResponseWrapper.success(accessToken));
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail(e.getMessage()));
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail(e.getMessage()));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.fail(e.getMessage()));
        }
    }

}
