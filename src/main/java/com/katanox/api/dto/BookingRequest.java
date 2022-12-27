package com.katanox.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class BookingRequest implements Serializable {
    private static final long serialVersionUID = 5560221391479816650L;

    @JsonProperty("hotelId")
    public long hotelId;

    @JsonProperty("roomId")
    public long roomId;

    @JsonProperty("price")
    public long price;

    @JsonProperty("checkin")
    public LocalDate checkin;

    @JsonProperty("checkout")
    public LocalDate checkout;

    @JsonProperty("currency")
    public String currency;

    @JsonProperty("guest")
    public Guest guest;

    @JsonProperty("payment")
    public Payment payment;
}
