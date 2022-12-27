package com.katanox.api.repository;

import com.katanox.test.sql.tables.Rooms;
import com.katanox.test.sql.tables.records.RoomsRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomsRepository {

    @Autowired
    private DSLContext dsl;
    public void insertRoom() {
        var room = Rooms.ROOMS;
        dsl.insertInto(room, room.hotels().ID, room.NAME)
                .values(1L, "fake")
                .execute();
    }

    public List<RoomsRecord> findRooms(Long hotelId) {
        var room = Rooms.ROOMS;
        return dsl.fetch(room, room.hotels().ID.eq(hotelId));
    }

}
