package ru.rsreu.contests_system.organization.event.participant_info.appeal;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.user.User;

@Data
@Builder
public class Appeal {
    @MongoId
    private ObjectId objectId;

    private String text;

    @DBRef
    private User organizer;

    private AppealStatus appealStatus;
}
