package ru.rsreu.contests_system.api.organization.event.resource.dto.event_info;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;
import ru.rsreu.contests_system.security.user.AuthenticationUserDetailMapper;

import java.util.List;

@Component
public record EventInfoMapper(UserService userService,
                              AuthenticationUserDetailMapper authenticationUserDetailMapper) {
    public EventInfoResponse toResponse(Event event, Authentication authentication) {
        System.out.println(authentication);
        return new EventInfoResponse(
                event.getId().toString(),
                event.getName(),
                event.getEventType(),
                event.getDescription(),
                event.getStartDateTime(),
                event.getEndDateTime(),
                getFollowingSign(event, authentication)
        );
    }

    private boolean getFollowingSign(Event event, Authentication authentication) {
        List<User> eventParticipants = event.getParticipantsInfos()
                .stream()
                .map(ParticipantInfo::getParticipant)
                .toList();
        return authentication != null
                && eventParticipants.contains(
                userService.getUserByEmail(
                        authenticationUserDetailMapper.toUserDetails(authentication).getUsername()));
    }
}
