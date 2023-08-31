package com.nashss.se.momentum.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;

public class LocalDateToStringConverter implements DynamoDBTypeConverter<String, LocalDate> {


    @Override
    public String convert(LocalDate localDate) {
        return localDate.toString();
    }

    @Override
    public LocalDate unconvert(String localDateString) {
        return LocalDate.parse(localDateString);
    }
}
