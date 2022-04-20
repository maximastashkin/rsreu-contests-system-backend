package ru.rsreu.contests_system.config.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
    @Override
    public Date convert(LocalDateTime source) {
        return Date.from(source.toInstant(ZoneOffset.UTC));
    }
}
