package ru.rsreu.contests_system.user.repository;

import java.util.List;

//Here write custom methods signatures
public interface UserCustomRepository {
    void addUserRefreshToken(String email, String refreshToken);

    long deleteUserRefreshToken(String email, String refreshToken);

    List<String> findRefreshTokensForUser(String email);
}
