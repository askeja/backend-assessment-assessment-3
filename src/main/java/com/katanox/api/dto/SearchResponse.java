package com.katanox.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class SearchResponse implements Serializable {
    private static final long serialVersionUID = 5560221991479016660L;

    @JsonProperty("hotelId")
    public long hotelId;

    @JsonProperty("roomPrices")
    List<RoomPricesDTO> roomPrices;

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List<RoomPricesDTO> getRoomPrices() {
        return roomPrices;
    }

    public void setRoomPrices(List<RoomPricesDTO> roomPrices) {
        this.roomPrices = roomPrices;
    }
}
