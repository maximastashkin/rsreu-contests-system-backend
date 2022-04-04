package ru.rsreu.contests_system.security.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rsreu.contests_system.api.user.User;

import java.util.Collection;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        username = user.getEmail();
        password = user.getPassword();
        authorities = user.getAuthorities().stream().map(authority ->
                    new SimpleGrantedAuthority(authority.toString())).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
