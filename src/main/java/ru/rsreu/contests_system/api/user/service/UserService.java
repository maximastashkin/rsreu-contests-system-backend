package ru.rsreu.contests_system.api.user.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.user.exception.NotUniqueEmailException;
import ru.rsreu.contests_system.api.user.repository.UserRepository;
import ru.rsreu.contests_system.api.user.User;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public record UserService(UserRepository userRepository) {
    public User save(User user) {
        try {
            return userRepository.save(user);
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
        return userRepository.findByEmail(email).orElseThrow();
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
}