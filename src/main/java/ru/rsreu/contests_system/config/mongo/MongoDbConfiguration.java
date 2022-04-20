package ru.rsreu.contests_system.config.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoDbConfiguration {
    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new LocalDateTimeToDateConverter(dateZoneOffset()));
        converters.add(new DateToLocalDateTimeConverter(dateZoneOffset()));
        return new MongoCustomConversions(converters);
    }

    @Bean
    public ZoneOffset dateZoneOffset() {
        return ZoneOffset.UTC;
    }
}
