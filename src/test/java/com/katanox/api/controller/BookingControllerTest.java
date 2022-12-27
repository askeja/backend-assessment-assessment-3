package com.katanox.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katanox.api.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @Test
    @DisplayName("When creating booking new booking id is returned")
    void createBooking() throws Exception {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.hotelId = 1;
        bookingRequest.roomId = 1;
        bookingRequest.checkin = LocalDate.parse("2022-03-01");
        bookingRequest.checkout = LocalDate.parse("2022-03-03");
        bookingRequest.currency = "EUR";
        bookingRequest.guest = mockGuest();
        bookingRequest.payment = mockPayment();
        BookingResponse response = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        post("/booking/")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(mapper.writeValueAsString(bookingRequest)))
                                .andExpect(status().isAccepted())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        BookingResponse.class);
        long bookingId = response.bookingId;
        assertThat(response.bookingId, greaterThan(0l));
        //delete booking
        mockMvc
                .perform(
                        delete("/booking/" + bookingId + "/")
                )
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("When deleting not existing booking NOT_FOUND status code returned")
    void deleteBooking() throws Exception {
        mockMvc
                .perform(
                        delete("/" + new Random().nextLong() + "/")
                )
                .andExpect(status().isNotFound());

    }

    private Payment mockPayment() {
        Payment payment = new Payment();
        payment.card_number = "1234-1234-1234-1234";
        payment.card_holder = "Jane Doe";
        payment.cvv = "123";
        payment.expiry_year = "2025";
        payment.expiry_month = "12";
        return payment;
    }

    private Guest mockGuest() {
        Guest guest = new Guest();
        guest.name = "Jane";
        guest.surname = "Doe";
        guest.birthdate =  LocalDate.parse("1990-03-03");
        return guest;
    }
}
