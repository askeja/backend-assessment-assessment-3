package com.katanox.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class RoomPricesDTO  implements Serializable {
    private static final long serialVersionUID = 5560561396279816650L;

    @JsonProperty("roomId")
    public long roomId;

    @JsonProperty("priceBeforeTax")
    public BigDecimal priceBeforeTax;

    @JsonProperty("priceAfterTax")
    public BigDecimal priceAfterTax;

    @JsonProperty("currency")
    public String currency;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public BigDecimal getPriceBeforeTax() {
        return priceBeforeTax;
    }

    public void setPriceBeforeTax(BigDecimal priceBeforeTax) {
        this.priceBeforeTax = priceBeforeTax;
    }

    public BigDecimal getPriceAfterTax() {
        return priceAfterTax;
    }

    public void setPriceAfterTax(BigDecimal priceAfterTax) {
        this.priceAfterTax = priceAfterTax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
