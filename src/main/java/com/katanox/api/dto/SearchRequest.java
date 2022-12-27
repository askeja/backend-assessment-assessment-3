package com.katanox.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class SearchRequest implements Serializable {
    private static final long serialVersionUID = 5560221391479016660L;

    @JsonProperty("checkin")
    public LocalDate checkin;

    @JsonProperty("checkout")
    public LocalDate checkout;

    @JsonProperty("hotelId")
    public long hotelId;
}