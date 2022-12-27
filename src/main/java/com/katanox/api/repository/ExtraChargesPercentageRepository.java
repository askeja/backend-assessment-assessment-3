package com.katanox.api.repository;

import com.katanox.test.sql.tables.ExtraChargesPercentage;
import com.katanox.test.sql.tables.records.ExtraChargesPercentageRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExtraChargesPercentageRepository {

    @Autowired
    private DSLContext dsl;
    public List<ExtraChargesPercentageRecord> findCharges(Long hotelId) {
        var charges = ExtraChargesPercentage.EXTRA_CHARGES_PERCENTAGE;
        return dsl.fetch(charges, charges.hotels().ID.eq(hotelId));
    }
}
