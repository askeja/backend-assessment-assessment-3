package com.katanox.api.service;

import com.katanox.api.dto.RoomPricesDTO;

import java.time.LocalDate;
import java.util.List;

public interface SearchService {
    List<RoomPricesDTO> findAvailable(long hotelId, LocalDate checkin, LocalDate checkout);
}
