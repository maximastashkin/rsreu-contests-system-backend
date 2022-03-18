package ru.rsreu.contests_system.security.refresh.black_list;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "tokens-black-list")
public class RefreshToken {
    @MongoId
    private ObjectId id;

    private String value;

    public RefreshToken(String value) {
        this.value = value;
    }
}
