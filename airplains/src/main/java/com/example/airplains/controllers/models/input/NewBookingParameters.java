package com.example.airplains.controllers.models.input;

import com.example.airplains.controllers.models.output.routes.RouteNodeDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class NewBookingParameters {
    @JsonProperty("passengerName")
    public String passengerName;

    @JsonProperty("flights")
    public List<RouteNodeDto> nodes;
}
