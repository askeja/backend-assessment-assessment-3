package com.katanox.api.repository;

import com.katanox.test.sql.tables.Hotels;
import com.katanox.test.sql.tables.records.HotelsRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HotelRepository {

    @Autowired
    private DSLContext dsl;

    public HotelsRecord findHotel(Long hotelId) {
        var hotel = Hotels.HOTELS;
        return dsl.fetchOne(hotel, hotel.ID.eq(hotelId));
    }

}
