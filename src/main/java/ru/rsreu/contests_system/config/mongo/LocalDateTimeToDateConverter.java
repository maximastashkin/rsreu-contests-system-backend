package ru.rsreu.contests_system.config.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class LocalDateTimeToDateConverter extends DateConverter implements Converter<LocalDateTime, Date> {
    public LocalDateTimeToDateConverter(ZoneOffset zoneOffset) {
        super(zoneOffset);
    }

    @Override
    public Date convert(LocalDateTime source) {
        return Date.from(source.toInstant(this.zoneOffset));
    }
}
