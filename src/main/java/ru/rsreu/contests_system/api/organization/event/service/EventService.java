package ru.rsreu.contests_system.api.organization.event.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class EventService {
    private final OrganizationRepository organizationRepository;

    public List<Event> getAllActualEvents(int pageSize, int pageNumber) {
        return organizationRepository.getAllActualEvents(PageRequest.of(pageNumber, pageSize));
    }
}
