package com.example.airplains.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @Column(name = "book_ref")
    private String book_ref;

    @Column(name = "book_date")
    private Date book_date;

    @Column(name = "total_amount")
    private double total_amount;
}