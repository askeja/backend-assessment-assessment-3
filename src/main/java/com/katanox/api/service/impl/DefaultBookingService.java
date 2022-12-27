package com.katanox.api.service.impl;

import com.katanox.api.dto.BookingRequest;
import com.katanox.api.messaging.RabbitMQBookingSenderService;
import com.katanox.api.dto.BookingDTO;
import com.katanox.api.dto.RoomPricesDTO;
import com.katanox.api.exception.ElementNotFoundException;
import com.katanox.api.repository.*;
import com.katanox.api.service.BookingService;
import com.katanox.api.service.utils.ChargesAggregator;
import com.katanox.test.sql.tables.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DefaultBookingService implements BookingService {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final RoomsRepository roomsRepository;
    private final PricesRepository pricesRepository;
    private final ExtraChargesFlatRepository extraChargesFlatRepository;
    private final ExtraChargesPercentageRepository extraChargesPercentageRepository;
    @Autowired
    RabbitMQBookingSenderService rabbitMQBookingSenderService;

    @Autowired
    LogWriterService logWriterService;

    public DefaultBookingService(HotelRepository hotelRepository,
                                 BookingRepository bookingRepository,
                                 RoomsRepository roomsRepository,
                                 PricesRepository pricesRepository,
                                 ExtraChargesFlatRepository extraChargesFlatRepository,
                                 ExtraChargesPercentageRepository extraChargesPercentageRepository,
                                 RabbitMQBookingSenderService rabbitMQBookingSenderService
    ) {
        this.hotelRepository = hotelRepository;
        this.bookingRepository = bookingRepository;
        this.roomsRepository = roomsRepository;
        this.pricesRepository = pricesRepository;
        this.extraChargesFlatRepository = extraChargesFlatRepository;
        this.extraChargesPercentageRepository = extraChargesPercentageRepository;
        this.rabbitMQBookingSenderService = rabbitMQBookingSenderService;
    }
    public BookingsRecord findBooking(long bookingId){
       return bookingRepository.findBooking(bookingId);
    }
    public int deleteBooking(long bookingId){
       return bookingRepository.deleteBooking(bookingId);
    }

    public long booking(BookingRequest bookingRequest){
        //Map to BookingDTO
        var bookingDTO = buildBookingDTO(bookingRequest);
        long bookingId = bookingRepository.insertBooking(bookingDTO);
        bookingDTO.bookingId = bookingId;
        rabbitMQBookingSenderService.ObjectRabbitMQSender(bookingDTO);
        return bookingId;
    }

    private BookingDTO buildBookingDTO(BookingRequest bookingRequest) {

        RoomPricesDTO roomPricesDTO = getRoomPrices(bookingRequest);

        var bookingDTO = new BookingDTO();
        bookingDTO.guest = bookingRequest.guest;
        bookingDTO.payment = bookingRequest.payment;
        bookingDTO.hotelId = bookingRequest.hotelId;
        bookingDTO.roomPricesDTO = roomPricesDTO;
        bookingDTO.checkin = bookingRequest.checkin;
        bookingDTO.checkout = bookingRequest.checkout;
        return bookingDTO;
    }

    private RoomPricesDTO getRoomPrices(BookingRequest bookingRequest) {
        HotelsRecord hotel = hotelRepository.findHotel(bookingRequest.hotelId);
        if (hotel == null) {
            logWriterService.logStringToConsoleOutput("Could not find hotel with ID : " + bookingRequest.hotelId);
            throw new ElementNotFoundException("Could not find hotel with ID provided");
        }

        List<PricesRecord> roomPricesList = pricesRepository.findPricesInRangeByRoom(bookingRequest.roomId, bookingRequest.checkin, bookingRequest.checkout);
        List<ExtraChargesFlatRecord> flatCharges = extraChargesFlatRepository.findCharges(bookingRequest.hotelId);
        List<ExtraChargesPercentageRecord> percentageCharges = extraChargesPercentageRepository.findCharges(bookingRequest.hotelId);

        int nights = (int) ChronoUnit.DAYS.between(bookingRequest.checkin, bookingRequest.checkout);
        ChargesAggregator chargesAggregator = new ChargesAggregator(roomPricesList, flatCharges, percentageCharges, hotel.getVat(), nights);
        chargesAggregator.processPricesAndCharges();

        RoomPricesDTO pricesDTO = new RoomPricesDTO();
        pricesDTO.setRoomId(bookingRequest.roomId);
        pricesDTO.setCurrency(hotel.getCurrency());
        pricesDTO.setPriceBeforeTax(chargesAggregator.getPriceAndChargesBeforeTax());
        pricesDTO.setPriceAfterTax(chargesAggregator.getPriceAndChargesAfterTax());
        return pricesDTO;
    }
}
