package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.not_unique_info;


import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfoException;

@Component
public class NotUniqueOrganizationInfoMapper {
    public NotUniqueOrganizationInfoResponse toResponse(NotUniqueOrganizationInfoException exception) {
        return new NotUniqueOrganizationInfoResponse(exception.getInfo());
    }
}
