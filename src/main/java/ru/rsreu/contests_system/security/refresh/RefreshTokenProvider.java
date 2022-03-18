package ru.rsreu.contests_system.security.refresh;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenProvider {
    private static final int REFRESH_TOKEN_BASE_LENGTH = 20;
    private static final int ACCESS_TOKEN_PART_LENGTH = 6;


    public String createTokenFromJwtToken(String jwtToken) {
        return RandomStringUtils.randomAscii(REFRESH_TOKEN_BASE_LENGTH) +
                StringUtils.right(jwtToken, ACCESS_TOKEN_PART_LENGTH);
    }

    public boolean validateRefreshTokenAndAccessToken(String refreshToken, String accessToken) {
        return StringUtils.right(refreshToken, ACCESS_TOKEN_PART_LENGTH).
                equals(StringUtils.right(accessToken, ACCESS_TOKEN_PART_LENGTH));
    }
}
