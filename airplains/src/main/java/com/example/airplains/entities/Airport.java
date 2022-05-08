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
@Table(name = "airports")
public class Airport {
    @Id
    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "airport_name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "timezone")
    private String timezone;
}
