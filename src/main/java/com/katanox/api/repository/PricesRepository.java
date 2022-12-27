package com.katanox.api.repository;

import com.katanox.test.sql.tables.Prices;
import com.katanox.test.sql.tables.records.PricesRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PricesRepository {
    @Autowired
    private DSLContext dsl;

    public Map<Long, List<PricesRecord>> findPricesInRangeByHotel(Long hotelId, LocalDate checkin, LocalDate checkout) {

        int nights = (int) ChronoUnit.DAYS.between(checkin, checkout);
        var price = Prices.PRICES;
        Map<Long, List<PricesRecord>> allPricesInRange = dsl.fetch(price,
                        price.rooms().hotels().ID.eq(hotelId),
                        price.DATE.between(checkin, checkout.minusDays(1)),
                        price.QUANTITY.greaterThan(0))
                .intoGroups(price.ROOM_ID, PricesRecord.class);
        Map<Long, List<PricesRecord>> pricesInFullRange = new HashMap<>();
        allPricesInRange.forEach( (roomId, prices) -> {
            if (prices.size() == nights) {
                pricesInFullRange.put(roomId, prices);
            }
        });
        return pricesInFullRange;
    }

    public List<PricesRecord> findPricesInRangeByRoom(Long roomId, LocalDate checkin, LocalDate checkout) {

        int nights = (int) ChronoUnit.DAYS.between(checkin, checkout);
        var price = Prices.PRICES;
        List<PricesRecord> pricesInRange = dsl.fetch(price,
                price.ROOM_ID.eq(roomId),
                price.DATE.between(checkin, checkout.minusDays(1)),
                price.QUANTITY.greaterThan(0));
        if (pricesInRange.size() == nights) {
             return pricesInRange;
        }
        return Collections.emptyList();
    }

}
