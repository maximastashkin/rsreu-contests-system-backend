package ru.rsreu.contests_system.api.user.exception;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.User;

@Component
public class UserExceptionsMessagesUtil {
    public String formNotUniqueEmailExceptionMessage(User user) {
        return String.format("Email:%s not unique!", user.getEmail());
    }

    public String formUserNotFoundExceptionByEmailMessage(String email) {
        return String.format("User with email:%s didn't found", email);
    }

    public String formAdminBlockingAttemptExceptionMessage() {
        return "Admin blocking is impossible";
    }

    public String formUserNotFoundByAuthenticationTokenException(String confirmationToken) {
        return String.format("User for confirmation token:%s not found", confirmationToken);
    }
}
