package com.example.airplains.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @Column(name = "aircraft_code")
    private String aircraftCode;

    @Column(name = "seat_no")
    private String seatNo;

    @Column(name = "fare_conditions")
    private String fareCondition;
}
