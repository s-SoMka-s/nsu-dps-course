package com.example.airplains.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.UUID;

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

    public Booking(){
        var calendar = Calendar.getInstance();

        this.book_ref = "_" + UUID.randomUUID().toString().substring(0, 5);
        this.book_date = new Date((calendar.getTime()).getTime());
        this.total_amount = 0d;
    }
}