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
    @Builder.Default private ObjectId objectId = new ObjectId();

    private String text;

    @DBRef
    private User organizer;

    private String organizerText;

    private AppealStatus appealStatus;
}
