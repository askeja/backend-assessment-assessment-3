package com.katanox.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katanox.api.dto.SearchRequest;
import com.katanox.api.dto.SearchResponse;
import com.katanox.api.repository.HotelRepository;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @Autowired private HotelRepository repository;

    @Test
    @DisplayName("When searching for rooms in hotel, return availabilities")
    void searchControllerTest() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.hotelId = 1;
        searchRequest.checkin = LocalDate.parse("2022-03-01");
        searchRequest.checkout = LocalDate.parse("2022-03-03");
        SearchResponse response = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        post("/search/")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(mapper.writeValueAsString(searchRequest)))
                                .andExpect(status().is2xxSuccessful())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        SearchResponse.class);
        System.out.println(response);

        assertThat( response.hotelId , equalTo(searchRequest.hotelId));
        assertThat( response.getRoomPrices().size() , equalTo(2));
        System.out.println(response.getRoomPrices().get(0).priceBeforeTax);
        System.out.println(response.getRoomPrices().get(0).priceAfterTax);
    }
}
