package com.nhom5.repository;

import com.nhom5.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    void save(Room room);

    void update(Room room);

    Optional<Room> findById(String roomId);

    List<Room> findAll();
}
