package fotcamp.finhub.common.security;

import fotcamp.finhub.admin.domain.Manager;
import fotcamp.finhub.admin.repository.ManagerRepository;
import fotcamp.finhub.common.domain.Member;
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
    private final ManagerRepository managerRepository;


    @Override
    public CustomUserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException{
        Member member = memberRepository.findByMemberUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
        CustomUserInfo dto = modelMapper.map(member, CustomUserInfo.class);
        System.out.println(dto.getUuid()+"******************************8");
        return new CustomUserDetails(dto);
    }

    public CustomUserDetails loadAdminByRole(String uuid) throws UsernameNotFoundException{
        Manager manager = managerRepository.findByManagerUuid(uuid).orElseThrow(
                () -> new UsernameNotFoundException("해당하는 관리자가 없습니다."));
        CustomUserInfo dto = modelMapper.map(manager, CustomUserInfo.class);
        return new CustomUserDetails(dto);
    }
}
