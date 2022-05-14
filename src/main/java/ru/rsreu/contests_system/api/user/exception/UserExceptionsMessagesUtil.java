package ru.rsreu.contests_system.api.user.exception;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.User;

@Component
public class UserExceptionsMessagesUtil {
    public String formNotUniqueEmailExceptionMessage(User user) {
        return String.format("Email:%s not unique!", user.getEmail());
    }

    public String formUserNotFoundExceptionByEmailMessage(String email) {
        return String.format("User with email:%s didn't find", email);
    }

    public String formUserNotFoundExceptionByIdMessage(ObjectId objectId) {
        return String.format("User with id:%s didn't find", objectId.toString());
    }

    public String formAdminBlockingAttemptExceptionMessage() {
        return "Admin blocking is impossible";
    }

    public String formUserNotFoundByAuthenticationTokenException(String confirmationToken) {
        return String.format("User for confirmation token:%s not found", confirmationToken);
    }
}
