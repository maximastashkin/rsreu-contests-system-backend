package ru.rsreu.contests_system.api.organization.event.resource.dto.event_creating;

import ru.rsreu.contests_system.api.organization.event.EventType;
import ru.rsreu.contests_system.validation.null_or_not_blank.NullOrNotBlank;
import ru.rsreu.contests_system.validation.object_id.NotRequiredObjectId;
import ru.rsreu.contests_system.validation.object_id.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EventCreatingRequest(
        @NotBlank
        String name,

        @NullOrNotBlank
        String description,

        @NotNull
        EventType eventType,

        @NotNull
        LocalDateTime startDateTime,

        @NotNull
        LocalDateTime endDateTime,

        @NotRequiredObjectId
        String eventLeaderId) {
        public String getDescription() {
                return description == null ? "" : description.trim();
        }
}
