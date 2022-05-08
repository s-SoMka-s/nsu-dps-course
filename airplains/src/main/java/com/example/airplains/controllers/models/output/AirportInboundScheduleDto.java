package com.example.airplains.controllers.models.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.sql.Date;

@Value
@Builder
@JsonDeserialize(builder = AirportInboundScheduleDto.AirportInboundScheduleBuilder.class)
public class AirportInboundScheduleDto {
    @JsonProperty("dayOfWeek")
    String dayOfWeek;

    @JsonProperty("timeOfArrival")
    Date scheduledArrival;

    @JsonProperty("flightNo")
    String flightNo;

    @JsonProperty("origin")
    String departureAirport;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AirportInboundScheduleBuilder {
    }
}
