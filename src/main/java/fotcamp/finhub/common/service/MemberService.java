package fotcamp.finhub.common.service;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.dto.SignupRequestDto;
import fotcamp.finhub.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public ResponseEntity<ApiResponseWrapper> signup(SignupRequestDto signupRequestDto){

        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        System.out.println(signupRequestDto.getPassword());
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        System.out.println(encodedPassword);
        // 회원 정보 저장
        Member member = SignupRequestDto.toEntity(signupRequestDto);
        memberRepository.save(member);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseWrapper.success("succeed"));
    }

    public ResponseEntity<ApiResponseWrapper> test1(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseWrapper.success("ok1"));
    }

    public ResponseEntity<ApiResponseWrapper> test2(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseWrapper.success("ok2"));
    }
}
