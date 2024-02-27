package fotcamp.finhub.common.security;

import fotcamp.finhub.main.domain.Member;
import fotcamp.finhub.main.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String id) throws UsernameNotFoundException{
        Member member = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

        CustomUserInfo dto = modelMapper.map(member, CustomUserInfo.class);
        return new CustomUserDetails(dto);
    }
}
