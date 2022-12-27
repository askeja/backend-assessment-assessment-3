package com.katanox.api.repository;

import com.katanox.test.sql.tables.ExtraChargesFlat;
import com.katanox.test.sql.tables.records.ExtraChargesFlatRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExtraChargesFlatRepository {

    @Autowired
    private DSLContext dsl;
    public List<ExtraChargesFlatRecord> findCharges(Long hotelId) {
        var charges = ExtraChargesFlat.EXTRA_CHARGES_FLAT;
        return dsl.fetch(charges, charges.hotels().ID.eq(hotelId));
    }

}
