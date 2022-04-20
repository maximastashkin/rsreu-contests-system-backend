package ru.rsreu.contests_system.api.organization_creating_application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public class NotUniqueOrganizationInfoException extends RuntimeException {
    private final EnumSet<NotUniqueOrganizationInfo> info;
}
