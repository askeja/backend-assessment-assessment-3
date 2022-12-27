package com.katanox.api.service.utils;

import com.katanox.test.sql.enums.AppliedOn;
import com.katanox.test.sql.enums.ChargeType;
import com.katanox.test.sql.tables.records.ExtraChargesFlatRecord;
import com.katanox.test.sql.tables.records.ExtraChargesPercentageRecord;
import com.katanox.test.sql.tables.records.PricesRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChargesAggregatorTest {
    @Test
    @DisplayName("When processing flat and percentage charges for rooms, calculate prices accordingly")
    void calculateTotalFlatCharges()  {
        List<ExtraChargesFlatRecord> flatCharges = new ArrayList<>();
        flatCharges.add(mockCharge(10.00, ChargeType.once));
        flatCharges.add(mockCharge(15.00, ChargeType.once));
        flatCharges.add(mockCharge(5.00, ChargeType.per_night));
        flatCharges.add(mockCharge(8.00, ChargeType.per_night));

        List<ExtraChargesPercentageRecord> percentageCharges = new ArrayList<>();
        percentageCharges.add(mockCharge(10.00, AppliedOn.total_amount));
        percentageCharges.add(mockCharge(15.00, AppliedOn.total_amount));
        percentageCharges.add(mockCharge(5.00, AppliedOn.first_night));
        percentageCharges.add(mockCharge(8.00, AppliedOn.first_night));

        List<PricesRecord> pricesList = new ArrayList<>();
        pricesList.add(mockPrice("2022-03-01", 1, BigDecimal.valueOf(100.00)));
        pricesList.add(mockPrice("2022-03-02", 1 ,BigDecimal.valueOf(110.00)));

        ChargesAggregator chargesAggregator = new ChargesAggregator(pricesList, flatCharges, percentageCharges, BigDecimal.valueOf(5), 2);
        chargesAggregator.processPricesAndCharges();
        assertThat(chargesAggregator.getPriceAndChargesBeforeTax(), equalTo(new BigDecimal(313.38).setScale(2, RoundingMode.HALF_EVEN)));
        assertThat(chargesAggregator.getPriceAndChargesAfterTax(), equalTo(new BigDecimal(323.38).setScale(2, RoundingMode.HALF_EVEN)));
    }

    private PricesRecord mockPrice(String date, long roomId, BigDecimal price) {
        PricesRecord pricesRecord = new PricesRecord();
        pricesRecord.setDate(LocalDate.parse(date));
        pricesRecord.setPriceAfterTax(price);
        pricesRecord.setCurrency("EUR");
        pricesRecord.setRoomId(roomId);
        return pricesRecord;
    }

    private ExtraChargesPercentageRecord mockCharge(double percentage, AppliedOn appliedOn) {
        ExtraChargesPercentageRecord charge = new ExtraChargesPercentageRecord();
        charge.setAppliedOn(appliedOn);
        charge.setPercentage(percentage);
        return charge;
    }

    private ExtraChargesFlatRecord mockCharge(double price, ChargeType chargeType) {
        ExtraChargesFlatRecord charge = new ExtraChargesFlatRecord();
        charge.setChargeType(chargeType);
        charge.setPrice(price);
        return charge;
    }
}
