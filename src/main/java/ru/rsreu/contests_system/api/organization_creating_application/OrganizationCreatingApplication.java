package ru.rsreu.contests_system.api.organization_creating_application;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@Document("applications")
public class OrganizationCreatingApplication {
    @MongoId
    private ObjectId id;

    @Indexed(unique = true)
    private String name;

    @Indexed(unique = true)
    private String organizationEmail;

    @Indexed(unique = true)
    private String organizationPhone;

    private String leaderFirstName;

    private String leaderLastName;

    private String leaderMiddleName;

    @Indexed(unique = true)
    private String leaderEmail;
}
