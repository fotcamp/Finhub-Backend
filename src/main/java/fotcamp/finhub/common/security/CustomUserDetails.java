package fotcamp.finhub.common.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final CustomUserInfo user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles =  new ArrayList<>();
        roles.add(user.getRole().toString());
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getUuid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRole(){
        return user.getRole().toString();
    }
    public Long getMemberIdasLong(){return user.getMemberId();}
}
