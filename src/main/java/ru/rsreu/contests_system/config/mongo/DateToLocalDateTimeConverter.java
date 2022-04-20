package ru.rsreu.contests_system.config.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateToLocalDateTimeConverter extends DateConverter implements Converter<Date, LocalDateTime> {
    public DateToLocalDateTimeConverter(ZoneOffset zoneOffset) {
        super(zoneOffset);
    }

    @Override
    public LocalDateTime convert(Date source) {
        return source.toInstant().atZone(this.zoneOffset).toLocalDateTime();
    }
}
