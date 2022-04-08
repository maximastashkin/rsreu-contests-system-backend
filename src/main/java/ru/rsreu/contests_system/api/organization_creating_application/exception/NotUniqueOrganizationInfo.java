package ru.rsreu.contests_system.api.organization_creating_application.exception;

public class NotUniqueOrganizationInfo extends RuntimeException {
    public NotUniqueOrganizationInfo(String message) {
        super(message);
    }
}
