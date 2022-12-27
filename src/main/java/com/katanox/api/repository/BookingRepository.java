package com.katanox.api.repository;

import com.katanox.api.dto.BookingDTO;
import com.katanox.test.sql.tables.Bookings;
import com.katanox.test.sql.tables.records.BookingsRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class BookingRepository {

    @Autowired
    private DSLContext dsl;
    public long insertBooking(BookingDTO bookingDTO) {
        var booking = Bookings.BOOKINGS;
        return dsl.insertInto(booking
                        , booking.ROOM_ID
                        , booking.CREATED_DATE
                        , booking.BOOKING_PRICE_BEFORE_TAX
                        , booking.BOOKING_PRICE_AFTER_TAX
                        , booking.BOOKING_CURRENCY
                        , booking.CHECKIN
                        , booking.CHECKOUT
                        , booking.GUEST_NAME
                        , booking.GUEST_SURNAME
                        , booking.GUEST_BIRTHDATE
                        , booking.PAYMENT_CARD_HOLDER
                        , booking.PAYMENT_CARD_NUMBER
                        , booking.PAYMENT_CARD_CVV
                        , booking.PAYMENT_EXPIRY_MONTH
                        , booking.PAYMENT_EXPIRY_YEAR)
                .values(bookingDTO.roomPricesDTO.roomId
                        , LocalDate.now()
                        , bookingDTO.roomPricesDTO.getPriceBeforeTax()
                        , bookingDTO.roomPricesDTO.getPriceAfterTax()
                        , bookingDTO.roomPricesDTO.currency
                        , bookingDTO.checkin
                        , bookingDTO.checkout
                        , bookingDTO.guest.name
                        , bookingDTO.guest.surname
                        , bookingDTO.guest.birthdate
                        , bookingDTO.payment.card_holder
                        , bookingDTO.payment.card_number
                        , bookingDTO.payment.cvv
                        , bookingDTO.payment.expiry_month
                        , bookingDTO.payment.expiry_year
                )
                .returningResult(booking.ID)
                .fetchOne()
                .value1();
    }

    public BookingsRecord findBooking(long bookingId) {
        var booking = Bookings.BOOKINGS;
        return dsl.fetchOne(booking, booking.ID.eq(bookingId));

    }
    public void updateBooking(long bookingId, String correlationId) {
        var booking = Bookings.BOOKINGS;
        dsl.update(booking).set(booking.CORRELATION_ID, correlationId)
                .where(booking.ID.eq(bookingId));

    }
    public int deleteBooking(long bookingId) {
        var booking = Bookings.BOOKINGS;
        return dsl.delete(booking)
                .where(booking.ID.eq(bookingId))
                        .execute();

    }

}
