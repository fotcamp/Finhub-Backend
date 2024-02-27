package fotcamp.finhub.main.service;


import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.main.domain.Member;
import fotcamp.finhub.common.domain.RefreshToken;
import fotcamp.finhub.common.security.CustomUserInfo;
import fotcamp.finhub.main.dto.request.LoginRequestDto;
import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.common.utils.JwtUtil;
import fotcamp.finhub.main.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenRepository tokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<ApiResponseWrapper> login(LoginRequestDto loginRequestDto){
        try{
            String email = loginRequestDto.getEmail();
            String password = loginRequestDto.getPassword();
            // 1. 회원가입 유무 확인
            Member member = memberRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));
            // 2. 비밀번호 일치 확인
            if(!passwordEncoder.matches(password,member.getPassword())){
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }
            CustomUserInfo info = modelMapper.map(member, CustomUserInfo.class);

            // 3. 로그인 성공으로 간주하고 access,refreshToken 생성
            TokenDto allTokens = jwtUtil.createAllTokens(info.getMemberId());

            tokenRepository.save(new RefreshToken(allTokens.getRefreshToken(), info.getEmail()));

            return ResponseEntity.ok(ApiResponseWrapper.success(allTokens));
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail(e.getMessage()));
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail(e.getMessage()));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponseWrapper> validRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("x-refreshToken");
        if(refreshToken!= null && jwtUtil.validateToken(refreshToken)){
            Long userId = jwtUtil.getUserId(refreshToken);
            String newAccessToken = jwtUtil.createToken(userId, "Access");
            return ResponseEntity.ok(ApiResponseWrapper.success(newAccessToken));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("헤더에 토큰이 없습니다."));
        }
    }

    // 로그아웃 처리
    // 토큰회전 방식 채택

}
