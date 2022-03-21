package ru.rsreu.contests_system;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
class ApplicationRunnerTests {

    @Configuration
    static class TestConfiguration {
        @Bean
        public MongoClient mongoClient() {
            return MongoClients.create("mongodb://rcs-db:1234@rcs-db:27017/rcs-db?authSource=rcs-db");
        }
    }

    @Autowired
    public MongoClient mongoClient;

    @Test
    void contextLoads() {
    }
}
