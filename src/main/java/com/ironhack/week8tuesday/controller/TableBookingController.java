package com.ironhack.week8tuesday.controller;

import com.ironhack.week8tuesday.model.TableBooking;
import com.ironhack.week8tuesday.repository.TableBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/table-bookings") // no obligatoria -> http://localhost:8080/api/table-bookings
public class TableBookingController {

    @Autowired
    private TableBookingRepository tableBookingRepository;

    @GetMapping("/{id}") // http://localhost:8080/api/table-bookings/1
    public ResponseEntity<TableBooking> getBookingById(@PathVariable("id") Long bookingId) {

        Optional<TableBooking> optionalTableBooking = tableBookingRepository.findById(bookingId);

        if (optionalTableBooking.isPresent()) {

            TableBooking foundBooking = optionalTableBooking.get();
            return ResponseEntity.ok(foundBooking);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping // http://localhost:8080/api/table-bookings
//    public List<TableBooking> getAllBookings() {
//        return tableBookingRepository.findAll();
//    }

    @GetMapping // http://localhost:8080/api/table-bookings
    public ResponseEntity<List<TableBooking>> getAllBookings() {
        List<TableBooking> tableBookings = tableBookingRepository.findAll();
        if (tableBookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(tableBookings);
        }
    }

    // /search?customerName=Pepito
    // /search?reservationDate=2024-11-09
    // /search?customerName=Pepito&reservationDate=2024-11-09

    @GetMapping("/search") // http://localhost:8080/api/table-bookings/search
    public List<TableBooking> getBookings(@RequestParam(required = false) String customerName,
                                          @RequestParam(required = false) LocalDate reservationDate) {

        if (customerName != null && reservationDate != null) {
            return tableBookingRepository.findAllByCustomerNameAndReservationDate(customerName, reservationDate);

        } else if (customerName != null) {
            return tableBookingRepository.findAllByCustomerName(customerName);

        } else if (reservationDate != null) {
            return tableBookingRepository.findAllByReservationDate(reservationDate);

        } else {
            return tableBookingRepository.findAll();
        }
        // lista de bookings -> por customerName, reservationDate, customerName & reservation, ningun filtro
    }

    @GetMapping("/sorted")
    public List<TableBooking> getSortedBookings(@RequestParam String sortBy,
                                                @RequestParam(required = false, defaultValue = "asc") String order){
        if ("desc".equalsIgnoreCase(order)){
            return tableBookingRepository.findAll(Sort.by(Sort.Direction.DESC, sortBy));
        } else {
            return tableBookingRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        }
    }
}
