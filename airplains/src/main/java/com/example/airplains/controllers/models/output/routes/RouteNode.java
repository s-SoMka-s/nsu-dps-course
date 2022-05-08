package com.example.airplains.controllers.models.output.routes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = RouteNode.RouteNodeBuilder.class)
public class RouteNode {

    String from;
    String to;
    String cityFrom;
    String cityTo;
    double flightId;
    double price;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RouteNodeBuilder {
    }
}
