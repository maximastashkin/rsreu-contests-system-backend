package ru.rsreu.contests_system.api.user.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.user.Authority;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.exception.AdminBlockingAttemptException;
import ru.rsreu.contests_system.api.user.exception.NotUniqueEmailException;
import ru.rsreu.contests_system.api.user.exception.UserExceptionsMessagesUtil;
import ru.rsreu.contests_system.api.user.exception.UserNotFoundException;
import ru.rsreu.contests_system.api.user.repository.UserRepository;
import ru.rsreu.contests_system.api.user.resource.dto.change_info.ChangeUserInfoRequest;
import ru.rsreu.contests_system.security.user.AuthenticationUserDetailMapper;

import java.util.List;

@Service
public record UserService(
        UserRepository userRepository,
        AuthenticationUserDetailMapper authenticationUserDetailMapper,
        UserExceptionsMessagesUtil userExceptionsMessagesUtil) {
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (RuntimeException exception) {
            throw new NotUniqueEmailException(userExceptionsMessagesUtil.formNotUniqueEmailExceptionMessage(user));
        }
    }

    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public List<User> getAll(int pageSize, int pageNumber) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize)).stream().toList();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(userExceptionsMessagesUtil
                        .formUserNotFoundExceptionByEmailMessage(email)));
    }

    public User getUserById(String id) {
        ObjectId objectId = new ObjectId(id);
        return userRepository.findById(objectId).orElseThrow(
                () -> new UserNotFoundException(userExceptionsMessagesUtil
                        .formUserNotFoundExceptionByIdMessage(objectId)));
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

    public void blockUserByEmail(String email) {
        User user = getUserByEmail(email);
        if (!user.getAuthorities().contains(Authority.ADMIN)) {
            replaceUserAuthority(user, Authority.UNBLOCKED, Authority.BLOCKED);
        } else {
            throw new AdminBlockingAttemptException(userExceptionsMessagesUtil
                    .formAdminBlockingAttemptExceptionMessage());
        }
    }

    public void unblockUserByEmail(String email) {
        replaceUserAuthority(getUserByEmail(email), Authority.BLOCKED, Authority.UNBLOCKED);
    }

    private void replaceUserAuthority(User user, Authority oldAuthority, Authority newAuthority) {
        user.getAuthorities().remove(oldAuthority);
        user.getAuthorities().add(newAuthority);
        save(user);
    }

    public void confirmUserByToken(String confirmationToken) {
        User user = userRepository.findByConfirmationToken(confirmationToken).orElseThrow(
                () -> new UserNotFoundException(
                        userExceptionsMessagesUtil.formUserNotFoundByAuthenticationTokenException(confirmationToken))
        );
        replaceUserAuthority(user, Authority.INACTIVE, Authority.ACTIVE);
        userRepository.unsetConfirmationToken(confirmationToken);
    }

    public User getUserByAuthentication(Authentication authentication) {
        return getUserByEmail(authenticationUserDetailMapper.toUserDetails(authentication).getUsername());
    }

    public void changeUserInfo(Authentication authentication, ChangeUserInfoRequest changeUserInfoRequest) {
        User user = getUserByAuthentication(authentication);
        user.setFirstName(changeUserInfoRequest.firstName());
        user.setLastName(changeUserInfoRequest.lastName());
        user.setMiddleName(changeUserInfoRequest.middleName());
        user.setEducationPlace(changeUserInfoRequest.educationPlace());
        save(user);
    }

    public User getEventLeader(String eventLeaderId, User initiator) {
        if (eventLeaderId != null) {
            return getUserById(eventLeaderId);
        }
        return initiator;
    }
}
