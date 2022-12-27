package com.katanox.api.controller;

import com.katanox.api.dto.RoomPricesDTO;
import com.katanox.api.service.SearchService;
import com.katanox.api.service.impl.LogWriterService;
import com.katanox.api.dto.SearchRequest;
import com.katanox.api.dto.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("search")
public class SearchController {

    @Value("${env}")
    String environment;

    @Autowired
    LogWriterService logWriterService;

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.logWriterService = new LogWriterService();
        this.searchService = searchService;
    }

    @GetMapping(path = "/ping")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<String> ping() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }


    @PostMapping(
            path = "/",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<SearchResponse> search( @RequestBody SearchRequest request ) {
        List<RoomPricesDTO> pricesDTOS = searchService.findAvailable(request.hotelId, request.checkin, request.checkout);
        SearchResponse response = new SearchResponse();
        response.setHotelId(request.hotelId);
        response.setRoomPrices(pricesDTOS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
