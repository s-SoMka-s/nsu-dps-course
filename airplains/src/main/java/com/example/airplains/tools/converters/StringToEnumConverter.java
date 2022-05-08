package com.example.airplains.tools.converters;

import com.example.airplains.entities.flights.FareConditions;
import org.springframework.core.convert.converter.Converter;


public class StringToEnumConverter implements Converter<String, FareConditions> {
    @Override
    public FareConditions convert(String source) {
        return FareConditions.valueOf(source.toUpperCase());
    }
}
