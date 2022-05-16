package ru.rsreu.contests_system.api.organization.event.resource.dto.leader_changing;

import ru.rsreu.contests_system.validation.object_id.NotRequiredObjectId;
import ru.rsreu.contests_system.validation.object_id.ObjectId;

public record EventLeaderChangingRequest(
        @ObjectId
        String eventId,
        @NotRequiredObjectId
        String newEventLeaderId) {
}
