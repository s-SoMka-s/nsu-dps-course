package com.example.airplains.controllers.models.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.sql.Date;

@Value
@Builder
@JsonDeserialize(builder = AirportOutboundScheduleDto.AirportOutboundScheduleBuilder.class)
public class AirportOutboundScheduleDto {
    @JsonProperty("dayOfWeek")
    String dayOfWeek;

    @JsonProperty("timeOfDeparture")
    Date scheduledDeparture;

    @JsonProperty("flightNo")
    String flightNo;

    @JsonProperty("destination")
    String arrivalAirport;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AirportOutboundScheduleBuilder {
    }
}
