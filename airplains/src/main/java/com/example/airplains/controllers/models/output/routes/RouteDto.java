package com.example.airplains.controllers.models.output.routes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.sql.Date;
import java.util.List;

@Value
@Builder
@JsonDeserialize(builder = RouteDto.RouteDtoBuilder.class)
public class RouteDto {

    String sourceAirport;
    String sourceCity;
    String destinationAirport;
    String destinationCity;
    Date departureDate;

    List<RouteNodeDto> routes;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RouteDtoBuilder {
    }
}