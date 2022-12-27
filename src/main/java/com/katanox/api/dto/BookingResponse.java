package com.katanox.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BookingResponse implements Serializable {
        private static final long serialVersionUID = 5560221391479816660L;

        @JsonProperty("bookingId")
        public long bookingId;

        @JsonProperty("message")
        public String message;

        @JsonCreator
        public BookingResponse(@JsonProperty("bookingId")long bookingId) {
                this.bookingId = bookingId;
        }
}




