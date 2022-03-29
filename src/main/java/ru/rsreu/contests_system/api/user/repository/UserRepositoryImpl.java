package ru.rsreu.contests_system.api.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.rsreu.contests_system.api.user.User;

import java.util.ArrayList;
import java.util.List;

//Here write custom methods impl.
public class UserRepositoryImpl implements UserCustomRepository {
    private static final String REFRESH_TOKENS_FIELD_NAME = "refreshTokens";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addUserRefreshToken(String email, String refreshToken) {
        mongoTemplate.updateFirst(
                getQueryByEmail(email),
                new Update().push(REFRESH_TOKENS_FIELD_NAME, refreshToken), User.class);
    }

    private Query getQueryByEmail(String email) {
        return Query.query(Criteria.where("email").is(email));
    }

    @Override
    public long deleteUserRefreshToken(String email, String refreshToken) {
        return mongoTemplate.updateFirst(
                getQueryByEmail(email),
                new Update().pull(REFRESH_TOKENS_FIELD_NAME, refreshToken), User.class).getModifiedCount();
    }

    @Override
    public List<String> findRefreshTokensForUser(String email) {
        Query query = getQueryByEmail(email);
        query.fields().include(REFRESH_TOKENS_FIELD_NAME);
        User user = mongoTemplate.findOne(query, User.class);
        return getUserRefreshTokens(user);
    }

    private List<String> getUserRefreshTokens(User user) {
        List<String> refreshTokens = new ArrayList<>();
        if (user != null && user.getRefreshTokens() != null) {
            refreshTokens = user.getRefreshTokens();
        }
        return refreshTokens;
    }
}
