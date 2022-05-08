package com.example.airplains.entities.flights;

import lombok.Getter;

@Getter
public enum FareConditions {
    ECONOMY("Economy"),
    COMFORT("Comfort"),
    BUSINESS("Business");

    FareConditions(String value){
        this.value = value;
    }

    private final String value;
}
