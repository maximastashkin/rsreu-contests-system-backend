package ru.rsreu.contests_system.api.user.repository;

import java.util.List;

public interface UserCustomRepository {
    void addUserRefreshToken(String email, String refreshToken);

    long deleteUserRefreshToken(String email, String refreshToken);

    List<String> findRefreshTokensForUser(String email);

    void unsetConfirmationToken(String confirmationToken);
}
