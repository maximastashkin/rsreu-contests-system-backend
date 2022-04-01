package ru.rsreu.contests_system.api.user.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.user.BlockedStatus;
import ru.rsreu.contests_system.api.user.Role;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.exception.AdminBlockingAttemptException;
import ru.rsreu.contests_system.api.user.exception.NotUniqueEmailException;
import ru.rsreu.contests_system.api.user.exception.UserNotFoundException;
import ru.rsreu.contests_system.api.user.repository.UserRepository;

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

    public void updateBlockedStatus(String id, BlockedStatus blockedStatus) {
        User user = getUserById(id);
        if (user.getRole() != Role.ADMIN) {
            user.setBlockedStatus(blockedStatus);
            userRepository.save(user);
        } else {
            throw new AdminBlockingAttemptException("Admin blocking is impossible");
        }
    }

    private User getUserById(String id) {
        User user;
        try {
            user = userRepository.findById(new ObjectId(id)).orElseThrow(IllegalArgumentException::new);
        } catch (IllegalArgumentException exception) {
            throw new UserNotFoundException(String.format("User with id: %s not found", id));
        }
        return user;
    }
}
