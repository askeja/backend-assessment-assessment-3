package com.katanox.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Payment implements Serializable {
    private static final long serialVersionUID = 5560561392379816650L;

    @JsonProperty("card_holder")
    public String card_holder;

    @JsonProperty("card_number")
    public String card_number;

    @JsonProperty("cvv")
    public String cvv;

    @JsonProperty("expiry_month")
    public String expiry_month;

    @JsonProperty("expiry_year")
    public String expiry_year;
}
