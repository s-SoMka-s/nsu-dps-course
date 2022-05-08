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
@Table(name = "aircraft")
public class Aircraft {
    @Id
    @Column(name = "aircraft_code")
    private String code;

    @Column(name = "model")
    private String model;

    @Column(name = "range")
    private int range;
}
