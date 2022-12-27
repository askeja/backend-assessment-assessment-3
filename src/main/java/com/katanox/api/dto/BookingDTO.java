package com.katanox.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

public class BookingDTO implements Serializable {

    private static final long serialVersionUID = 5560221397679816650L;

    @JsonProperty("bookingId")
    public long bookingId;

    @JsonProperty("hotelId")
    public long hotelId;

    @JsonProperty("roomPricesDTO")
    public RoomPricesDTO roomPricesDTO;

    @JsonProperty("checkin")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate checkin;

    @JsonProperty("checkout")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate checkout;

    @JsonProperty("guest")
    public Guest guest;

    @JsonProperty("payment")
    public Payment payment;
}
