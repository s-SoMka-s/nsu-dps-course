package com.example.airplains.controllers.models.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {
    @JsonProperty("ticketNumber")
    String ticketNo;

    @JsonProperty("flightId")
    Integer flightId;

    public BookingDto(Integer flightId, String ticketNo) {
        this.ticketNo = ticketNo;
        this.flightId = flightId;
    }
}
