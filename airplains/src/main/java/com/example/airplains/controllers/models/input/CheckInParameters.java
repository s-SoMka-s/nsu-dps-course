package com.example.airplains.controllers.models.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CheckInParameters {
    @JsonProperty("ticketNumber")
    public String ticketNo;
}
