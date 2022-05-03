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
        converters.add(new LocalDateTimeToDateConverter(ZoneOffset()));
        converters.add(new DateToLocalDateTimeConverter(ZoneOffset()));
        return new MongoCustomConversions(converters);
    }

    private ZoneOffset ZoneOffset() {
        return ZoneOffset.UTC;
    }

    @Bean
    public ZoneOffset moscowZoneOffset() {
        return ZoneOffset.ofHours(3);
    }
}
