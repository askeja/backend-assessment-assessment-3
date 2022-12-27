package com.katanox.api.service.utils;

import com.katanox.test.sql.enums.AppliedOn;
import com.katanox.test.sql.enums.ChargeType;
import com.katanox.test.sql.tables.records.ExtraChargesFlatRecord;
import com.katanox.test.sql.tables.records.ExtraChargesPercentageRecord;
import com.katanox.test.sql.tables.records.PricesRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class ChargesAggregator {
    List<PricesRecord> roomPricesList;
    List<ExtraChargesFlatRecord> flatCharges;
    List<ExtraChargesPercentageRecord> percentageCharges;
    BigDecimal vat;
    BigDecimal totalPriceBeforeTax = BigDecimal.ZERO;
    BigDecimal totalTax = BigDecimal.ZERO;
    BigDecimal totalCharges = BigDecimal.ZERO;

    int nights = 0;

    public ChargesAggregator(List<PricesRecord> roomPricesList, List<ExtraChargesFlatRecord> flatCharges, List<ExtraChargesPercentageRecord> percentageCharges, BigDecimal vat, int nights) {
        this.roomPricesList = roomPricesList;
        this.flatCharges = flatCharges;
        this.percentageCharges = percentageCharges;
        this.vat = vat;
        this.nights = nights;
    }

    public BigDecimal getPriceAndChargesBeforeTax() {
        return totalPriceBeforeTax.add(totalCharges).setScale(2, RoundingMode.HALF_EVEN);
    }
    public BigDecimal getPriceAndChargesAfterTax() {
        return getPriceAndChargesBeforeTax().add(totalTax).setScale(2, RoundingMode.HALF_EVEN);
    }

    private void addToTotalPriceBeforeTax(BigDecimal price) {
        this.totalPriceBeforeTax = totalPriceBeforeTax.add(price);;
    }
    private void addToTotalTax(BigDecimal tax) {
        this.totalTax = totalTax.add(tax);;
    }

    private void addToCharges(BigDecimal tax) {
        this.totalCharges = totalCharges.add(tax);;
    }

    public void processPricesAndCharges() {
        List<ExtraChargesPercentageRecord> singlePercentageCharges = percentageCharges.stream()
                .filter(charge -> charge.getAppliedOn().equals(AppliedOn.first_night)).collect(Collectors.toList());
        List<ExtraChargesPercentageRecord> totalPercentageCharges = percentageCharges.stream()
                .filter(charge -> charge.getAppliedOn().equals(AppliedOn.total_amount)).collect(Collectors.toList());

        for (int i = 0; i < roomPricesList.size(); i++) {
            PricesRecord price = roomPricesList.get(i);
            BigDecimal priceAfterTax = price.getPriceAfterTax();
            BigDecimal priceBeforeTax = calculatePriceBeforeTax(priceAfterTax);
            addToTotalTax(priceAfterTax.subtract(priceBeforeTax));
            addToTotalPriceBeforeTax(priceBeforeTax);
            if (i == 0) {
                //add percentage charges on the first night
                for (ExtraChargesPercentageRecord singleCharge : singlePercentageCharges) {
                    addToCharges(calculatePercentage(priceBeforeTax, singleCharge.getPercentage()));
                }
            }
        }
        for (ExtraChargesPercentageRecord totalCharge : totalPercentageCharges) {
            //add percentage charges on the total amount
            addToCharges(calculatePercentage(totalPriceBeforeTax, totalCharge.getPercentage()));
        }
        addToCharges(calculateTotalFlatCharges());
    }

    private BigDecimal calculatePercentage(BigDecimal amount, Double percentage) {
        return amount.multiply(BigDecimal.valueOf(percentage).divide(BigDecimal.valueOf(100)));
    }

    private BigDecimal calculatePriceBeforeTax(BigDecimal priceAfterTax) {
        BigDecimal vatPercentage = vat.divide(BigDecimal.valueOf(100));
        return priceAfterTax.divide(BigDecimal.ONE.add(vatPercentage), 2, RoundingMode.HALF_EVEN);
    }
    private BigDecimal calculateTotalFlatCharges() {
        BigDecimal totalFlatCharges = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        for (ExtraChargesFlatRecord charge:flatCharges) {
            if (charge.getChargeType().equals(ChargeType.once)) {
                totalFlatCharges = totalFlatCharges.add(BigDecimal.valueOf(charge.getPrice()));
            } else {
                totalFlatCharges = totalFlatCharges.add(BigDecimal.valueOf(charge.getPrice()).multiply(BigDecimal.valueOf(nights)));
            }
        }
        return totalFlatCharges;
    }

}
