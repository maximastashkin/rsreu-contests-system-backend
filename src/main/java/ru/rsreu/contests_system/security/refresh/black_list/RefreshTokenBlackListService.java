package ru.rsreu.contests_system.security.refresh.black_list;

import org.springframework.stereotype.Service;

@Service
public record RefreshTokenBlackListService(
        RefreshTokenBlackListRepository refreshTokenBlackListRepository) {

    public void addToBlackList(String refreshToken) {
        refreshTokenBlackListRepository.save(new RefreshToken(refreshToken));
    }

    public boolean containsInBlackList(String refreshToken) {
        return refreshTokenBlackListRepository
                .findAll()
                .stream().map(RefreshToken::getValue)
                .toList().contains(refreshToken);
    }
}
