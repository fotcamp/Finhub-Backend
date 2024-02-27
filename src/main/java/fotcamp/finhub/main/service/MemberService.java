package fotcamp.finhub.main.service;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.main.domain.Member;
import fotcamp.finhub.main.dto.request.SignupRequestDto;
import fotcamp.finhub.main.repository.MemberRepository;
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

    public ResponseEntity<ApiResponseWrapper> signup(SignupRequestDto signupRequestDto){ //USER 회원가입

        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {

            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        System.out.println(encodedPassword);
        // 회원 정보 저장
        Member member = SignupRequestDto.toEntity1(signupRequestDto,passwordEncoder);
        memberRepository.save(member);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseWrapper.success("succeed"));
    }

    public ResponseEntity<ApiResponseWrapper> test1(){
        return ResponseEntity.ok(ApiResponseWrapper.success("ok1"));
    }

    public ResponseEntity<ApiResponseWrapper> test2(){
        return ResponseEntity.ok(ApiResponseWrapper.success("ok2"));
    }
}
