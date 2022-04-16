package ru.rsreu.contests_system.api.organization_creating_application.exception;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public class NotUniqueOrganizationInfoException extends RuntimeException {
    private final EnumSet<NotUniqueOrganizationInfo> info;

    public NotUniqueOrganizationInfoException(EnumSet<NotUniqueOrganizationInfo> info) {
        this.info = info;
    }
}
