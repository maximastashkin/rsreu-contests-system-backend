package ru.rsreu.contests_system.security.refresh;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.security.jwt.JwtTokenProvider;
import ru.rsreu.contests_system.security.refresh.black_list.RefreshTokenBlackListService;
import ru.rsreu.contests_system.api.user.service.UserService;

@Service
public record RefreshService(UserService userService,
                             UserDetailsService userDetailsService,
                             RefreshTokenProvider refreshTokenProvider,
                             JwtTokenProvider jwtTokenProvider,
                             RefreshTokenBlackListService refreshTokenBlackListService) {

    public boolean isValidRefreshing(String username, String accessToken, String refreshToken) {
        return refreshTokenProvider.validateRefreshTokenAndAccessToken(refreshToken, accessToken)
                && isSuchRefreshTokenForUser(username, refreshToken);
    }

    public boolean isRefreshTokenStructure(String refreshToken, String accessToken) {
        return refreshTokenProvider.validateRefreshTokenAndAccessToken(refreshToken, accessToken);
    }

    public String generateNewAccessToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenProvider.createTokenFromAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        ));
    }

    public String generateNewRefreshToken(String jwtToken) {
        return refreshTokenProvider.createTokenFromJwtToken(jwtToken);
    }

    public void addUserRefreshToken(String username, String refreshToken) {
        userService.addRefreshToken(username, refreshToken);
    }

    public long deleteUserRefreshToken(String username, String refreshToken) {
        return userService.deleteRefreshToken(username, refreshToken);
    }

    private boolean isSuchRefreshTokenForUser(String username, String refreshToken) {
        return userService.getRefreshTokens(username).contains(refreshToken);
    }

    public boolean isCompromisedRefreshToken(String refreshToken) {
        return !refreshTokenBlackListService.containsInBlackList(refreshToken);
    }
}
