package ru.rsreu.contests_system.config.mongo;

import org.apache.tomcat.jni.Local;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class StringToMongoLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
    private static final TypeDescriptor SOURCE = TypeDescriptor.valueOf(String.class);

    private static final TypeDescriptor TARGET = TypeDescriptor.valueOf(MongoLocalDateTime.class);

    @Override
    public LocalDateTime convert(Date source) {
        return source.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
    }
}
