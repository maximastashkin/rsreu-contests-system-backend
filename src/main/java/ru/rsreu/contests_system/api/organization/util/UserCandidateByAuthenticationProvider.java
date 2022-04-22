package ru.rsreu.contests_system.api.organization.util;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;
import ru.rsreu.contests_system.security.user.AuthenticationUserDetailMapper;

import java.util.Optional;

@Component
public record UserCandidateByAuthenticationProvider(UserService userService,
                                                    AuthenticationUserDetailMapper authenticationUserDetailMapper) {
    public Optional<User> getCandidateForMapping(Authentication authentication) {
        return authentication != null ? Optional.of(userService.getUserByEmail(
                authenticationUserDetailMapper.toUserDetails(authentication).getUsername())) : Optional.empty();
    }
}
