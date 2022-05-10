package com.example.airplains.controllers.models.input;

import com.example.airplains.entities.flights.FareConditions;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Value
public class SelectedFlightParameters {
    @JsonProperty("flightId")
    Integer flightId;

    @JsonProperty("amount")
    Double amount;

    @JsonProperty("fareConditions")
    FareConditions fareConditions;
}
