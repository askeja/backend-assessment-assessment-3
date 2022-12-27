package com.katanox.api.service;

import com.katanox.api.dto.BookingRequest;
import com.katanox.test.sql.tables.records.BookingsRecord;

public interface BookingService {
    long booking(BookingRequest bookingRequest);
    BookingsRecord findBooking(long bookingId);
    int deleteBooking(long bookingId);
}
