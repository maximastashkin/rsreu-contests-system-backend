package ru.rsreu.contests_system.api.organization.repository;

import org.springframework.data.domain.Pageable;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

import java.util.List;

public interface OrganizationCustomRepository {
    List<Event> findAllActualEvents(Pageable pageable);

    List<Event> findUserAllActualEvents(User user, Pageable pageable);

    List<Event> findUserAllCompletedEvents(User user, Pageable pageable);
}
