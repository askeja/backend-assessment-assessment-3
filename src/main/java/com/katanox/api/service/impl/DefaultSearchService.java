package com.katanox.api.service.impl;

import com.katanox.api.dto.RoomPricesDTO;
import com.katanox.api.exception.ElementNotFoundException;
import com.katanox.api.repository.*;
import com.katanox.api.service.SearchService;
import com.katanox.api.service.utils.ChargesAggregator;
import com.katanox.test.sql.tables.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class DefaultSearchService implements SearchService {

    private final HotelRepository hotelRepository;
    private final RoomsRepository roomsRepository;
    private final PricesRepository pricesRepository;
    private final ExtraChargesFlatRepository extraChargesFlatRepository;
    private final ExtraChargesPercentageRepository extraChargesPercentageRepository;

    @Autowired
    LogWriterService logWriterService;
    public DefaultSearchService(HotelRepository hotelRepository,
                                RoomsRepository roomsRepository,
                                PricesRepository pricesRepository,
                                ExtraChargesFlatRepository extraChargesFlatRepository,
                                ExtraChargesPercentageRepository extraChargesPercentageRepository
    ) {
        this.hotelRepository = hotelRepository;
        this.roomsRepository = roomsRepository;
        this.pricesRepository = pricesRepository;
        this.extraChargesFlatRepository = extraChargesFlatRepository;
        this.extraChargesPercentageRepository = extraChargesPercentageRepository;
    }

    @Override
    public List<RoomPricesDTO> findAvailable(long hotelId, LocalDate checkin, LocalDate checkout) {

        HotelsRecord hotel = hotelRepository.findHotel(hotelId);
        if (hotel == null) {
            logWriterService.logStringToConsoleOutput("Could not find hotel with ID : " + hotelId);
            throw new ElementNotFoundException("Could not find hotel with ID provided");
        }
        int nights = (int) ChronoUnit.DAYS.between(checkin, checkout);
        BigDecimal vat = hotel.getVat();

        Map<Long, List<PricesRecord>> availablePrices = pricesRepository.findPricesInRangeByHotel(hotelId, checkin, checkout);
        //find and add extra charges
        List<ExtraChargesFlatRecord> flatCharges = extraChargesFlatRepository.findCharges(hotelId);//per night / once
        List<ExtraChargesPercentageRecord> percentageCharges = extraChargesPercentageRepository.findCharges(hotelId); // firstnight/totalamnt
        List<RoomPricesDTO> roomPrices =  new LinkedList<>();
        availablePrices.forEach((roomId, prices)-> {
            createListOfRoomPrices(hotel, prices, roomId, nights, flatCharges, percentageCharges, roomPrices);
        });
        return roomPrices;
     }

    private void createListOfRoomPrices(HotelsRecord hotel, List<PricesRecord> prices, Long roomId, int nights, List<ExtraChargesFlatRecord> flatCharges, List<ExtraChargesPercentageRecord> percentageCharges, List<RoomPricesDTO> roomPrices) {
        ChargesAggregator chargesAggregator = new ChargesAggregator(prices, flatCharges, percentageCharges, hotel.getVat(), nights);
        chargesAggregator.processPricesAndCharges();
        RoomPricesDTO pricesDTO = new RoomPricesDTO();
        pricesDTO.setRoomId(roomId);
        pricesDTO.setCurrency(hotel.getCurrency());
        pricesDTO.setPriceBeforeTax(chargesAggregator.getPriceAndChargesBeforeTax());
        pricesDTO.setPriceAfterTax(chargesAggregator.getPriceAndChargesAfterTax());
        roomPrices.add(pricesDTO);
    }
}
