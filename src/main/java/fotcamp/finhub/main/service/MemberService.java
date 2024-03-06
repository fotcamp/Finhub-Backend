package fotcamp.finhub.main.service;

import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.main.domain.Member;
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


}
