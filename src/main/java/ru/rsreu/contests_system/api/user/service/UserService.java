package ru.rsreu.contests_system.api.user.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.user.Authority;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.exception.AdminBlockingAttemptException;
import ru.rsreu.contests_system.api.user.exception.NotUniqueEmailException;
import ru.rsreu.contests_system.api.user.exception.UserNotFoundException;
import ru.rsreu.contests_system.api.user.repository.UserRepository;
import ru.rsreu.contests_system.security.user.AuthenticationUserDetailMapper;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public record UserService(
        UserRepository userRepository,
        AuthenticationUserDetailMapper authenticationUserDetailMapper) {
    public void save(User user) {
        try {
            userRepository.save(user);
        } catch (RuntimeException exception) {
            throw new NotUniqueEmailException(String.format("Email:%s not unique!", user.getEmail()));
        }
    }

    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public List<User> getAll(int pageSize, int pageNumber) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize)).stream().toList();
    }

    public User getUserByEmail(String email) throws NoSuchElementException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(String.format("User with email:%s didn't found", email)));
    }

    public void addRefreshToken(String email, String refreshToken) {
        userRepository.addUserRefreshToken(email, refreshToken);
    }

    public long deleteRefreshToken(String email, String refreshToken) {
        return userRepository.deleteUserRefreshToken(email, refreshToken);
    }

    public List<String> getRefreshTokens(String email) {
        return userRepository.findRefreshTokensForUser(email);
    }

    public void blockUserByEmail(String email) {
        User user = getUserByEmail(email);
        if (!user.getAuthorities().contains(Authority.ADMIN)) {
            replaceUserAuthority(user, Authority.UNBLOCKED, Authority.BLOCKED);
        } else {
            throw new AdminBlockingAttemptException("Admin blocking is impossible");
        }
    }

    public void unblockUserByEmail(String email) {
        replaceUserAuthority(getUserByEmail(email), Authority.BLOCKED, Authority.UNBLOCKED);
    }

    private void replaceUserAuthority(User user, Authority oldAuthority, Authority newAuthority) {
        user.getAuthorities().remove(oldAuthority);
        user.getAuthorities().add(newAuthority);
        save(user);
    }

    public void confirmUserByToken(String confirmationToken) {
        User user = userRepository.findByConfirmationToken(confirmationToken).orElseThrow(
                () -> new UserNotFoundException(String.format("User for confirmation token:%s not found", confirmationToken))
        );
        replaceUserAuthority(user, Authority.INACTIVE, Authority.ACTIVE);
        userRepository.unsetConfirmationToken(confirmationToken);
    }

    public User getUserByAuthentication(Authentication authentication) {
        return getUserByEmail(authenticationUserDetailMapper.toUserDetails(authentication).getUsername());
    }
}
