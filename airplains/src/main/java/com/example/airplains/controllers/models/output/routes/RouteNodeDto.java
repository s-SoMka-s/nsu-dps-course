package com.example.airplains.controllers.models.output.routes;

import com.example.airplains.entities.flights.FareConditions;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Value
@Builder
@JsonDeserialize(builder = RouteNodeDto.RouteNodeBuilder.class)
public class RouteNodeDto {
    List<RouteNodeDto> children;

    String departureAirport;

    String arrivalAirport;

    String departureCity;

    String arrivalCity;

    Date departureDate;

    Date arrivalDate;

    Integer flightId;

    String amount;

    FareConditions fareConditions;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RouteNodeBuilder {
    }
}
