package com.ironhack.week8tuesday.repository;

import com.ironhack.week8tuesday.model.TableBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TableBookingRepository extends JpaRepository<TableBooking, Long> {

    List<TableBooking> findAllByCustomerNameAndReservationDate(String customerName, LocalDate reservationDate);

    List<TableBooking> findAllByCustomerName(String customerName);

    List<TableBooking> findAllByReservationDate(LocalDate reservationDate);
}
