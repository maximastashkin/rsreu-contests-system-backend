package ru.rsreu.contests_system.api.organization.repository;

import org.springframework.data.domain.Pageable;
import ru.rsreu.contests_system.api.organization.event.Event;

import java.util.List;

public interface OrganizationCustomRepository {
    List<Event> getAllActualEvents(Pageable pageable);
}
