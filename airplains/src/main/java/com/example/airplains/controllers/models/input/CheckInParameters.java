package com.example.airplains.controllers.models.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CheckInParameters {
    @JsonProperty("ticketNo")
    private String ticketNo;
}
