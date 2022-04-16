package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.not_unique_info;

import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfo;

import java.util.EnumSet;

public record NotUniqueOrganizationInfoResponse(EnumSet<NotUniqueOrganizationInfo> notUniqueCause) {
}
