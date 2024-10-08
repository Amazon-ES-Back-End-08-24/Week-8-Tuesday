package com.ironhack.week8tuesday.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "table-bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private LocalDate reservationDate;

    private Integer numberOfGuests;

    public TableBooking(String customerName, LocalDate reservationDate, Integer numberOfGuests) {
        this.customerName = customerName;
        this.reservationDate = reservationDate;
        this.numberOfGuests = numberOfGuests;
    }
}
