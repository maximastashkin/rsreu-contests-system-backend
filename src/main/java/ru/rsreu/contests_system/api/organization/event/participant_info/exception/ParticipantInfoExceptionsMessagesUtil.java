package ru.rsreu.contests_system.api.organization.event.participant_info.exception;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.User;

@Component
public class ParticipantInfoExceptionsMessagesUtil {
    public String formParticipantInfoNotFoundExceptionMessage(User participant) {
        return String.format("Critical error. Server didn't create ParticipantInfo for user with email:%s",
                participant.getEmail());
    }
}
