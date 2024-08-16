package fotcamp.finhub.main.service;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.main.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 유저의 이메일정보를 설정한다.
     * */
    public void modifyEmail(String email, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.setEmail(email);
        memberRepository.save(member);
    }
}
