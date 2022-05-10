package com.example.airplains.entities.flights;

import lombok.Getter;

@Getter
public enum FareConditions {
    Economy("Economy"),
    Comfort("Comfort"),
    Business("Business");

    FareConditions(String value){
        this.value = value;
    }

    private final String value;
}
