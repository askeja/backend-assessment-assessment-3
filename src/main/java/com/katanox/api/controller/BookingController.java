package com.katanox.api.controller;


import com.katanox.api.dto.BookingRequest;
import com.katanox.api.dto.BookingResponse;
import com.katanox.api.exception.ElementNotFoundException;
import com.katanox.api.service.BookingService;
import com.katanox.test.sql.tables.records.BookingsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping(
            path = "/",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    ResponseEntity<BookingResponse> booking( @RequestBody BookingRequest request) {
        long bookingId = bookingService.booking(request);
        BookingResponse response = new BookingResponse(bookingId);
        response.message = getBookingStateMessage(bookingId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private String getBookingStateMessage(long bookingId) {
        BookingsRecord booking = bookingService.findBooking(bookingId);
        if (booking.getCorrelationId() == null) {
            return "Booking queued in third party booking service.";
        }
        return "Booking processed in third party booking service.";
    }

    @DeleteMapping(path = "/{bookingId}")
    ResponseEntity deleteBooking(@PathVariable("bookingId") long bookingId ) {
        var result = bookingService.deleteBooking(bookingId);
        if (result == 0) {
            throw new ElementNotFoundException("Could not find booking with ID provided");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
