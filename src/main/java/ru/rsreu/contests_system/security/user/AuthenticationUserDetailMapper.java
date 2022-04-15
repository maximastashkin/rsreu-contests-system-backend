package ru.rsreu.contests_system.security.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUserDetailMapper {
    public UserDetails toUserDetails(Authentication authentication) {
        return (UserDetails) authentication.getPrincipal();
    }
}
