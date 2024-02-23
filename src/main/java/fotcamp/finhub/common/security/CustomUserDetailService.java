package fotcamp.finhub.common.security;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.dto.common.CustomUserInfoDto;
import fotcamp.finhub.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException{
        Member member = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

        CustomUserInfoDto dto = modelMapper.map(member, CustomUserInfoDto.class);
        return new CustomUserDetails(dto);
    }
}
