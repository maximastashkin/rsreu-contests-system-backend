package ru.rsreu.contests_system.api.organization.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;
import ru.rsreu.contests_system.security.user.AuthenticationUserDetailMapper;

import java.util.List;

@Service
public record EventService(
        OrganizationRepository organizationRepository,
        UserService userService,
        AuthenticationUserDetailMapper authenticationUserDetailMapper) {

    public List<Event> getAllActualEvents(int pageSize, int pageNumber) {
        return organizationRepository.getAllActualEvents(PageRequest.of(pageNumber, pageSize));
    }

    public List<Event> getAllUserActualEvents(Authentication authentication, int pageSize, int pageNumber) {
        User user = userService.getUserByEmail(
                authenticationUserDetailMapper.toUserDetails(authentication).getUsername());
        return organizationRepository.getAllUserActualEvents(user, PageRequest.of(pageNumber, pageSize));
    }
}
