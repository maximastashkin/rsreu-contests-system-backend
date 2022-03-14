package ru.rsreu.contests_system.user.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.user.User;
import ru.rsreu.contests_system.user.repository.UserRepository;

import java.util.List;

@Service
public record UserService(UserRepository userRepository) {
    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public List<User> getAll(int pageSize, int pageNumber) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize)).stream().toList();
    }
}
