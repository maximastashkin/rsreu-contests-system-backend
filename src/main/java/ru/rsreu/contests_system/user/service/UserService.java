package ru.rsreu.contests_system.user.service;

import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.user.User;
import ru.rsreu.contests_system.user.repository.UserRepository;

@Service
public record UserService(UserRepository userRepository) {
    public User save(User user) {
        return userRepository.save(user);
    }
}
