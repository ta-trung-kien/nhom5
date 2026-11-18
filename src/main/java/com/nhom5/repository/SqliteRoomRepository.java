package com.nhom5.repository;

import com.nhom5.model.Room;
import com.nhom5.model.RoomFactory;
import com.nhom5.model.RoomStatus;
import com.nhom5.model.RoomType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SqliteRoomRepository implements RoomRepository {

    private final DatabaseManager database;

    public SqliteRoomRepository(DatabaseManager database) {
        this.database = database;
    }

    @Override
    public void save(Room room) {
        database.update(
                "INSERT INTO rooms(room_id, room_name, floor, capacity, room_type, status) VALUES (?, ?, ?, ?, ?, ?)",
                room.getRoomId(),
                room.getRoomName(),
                room.getFloor(),
                room.getCapacity(),
                room.getRoomType().name(),
                room.getStatus().name()
        );
    }

    @Override
    public void update(Room room) {
        database.update(
                "UPDATE rooms SET room_name = ?, floor = ?, capacity = ?, room_type = ?, status = ? WHERE room_id = ?",
                room.getRoomName(),
                room.getFloor(),
                room.getCapacity(),
                room.getRoomType().name(),
                room.getStatus().name(),
                room.getRoomId()
        );
    }

    @Override
    public Optional<Room> findById(String roomId) {
        return database.query("SELECT * FROM rooms WHERE room_id = ?", this::map, roomId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Room> findAll() {
        return database.query("SELECT * FROM rooms ORDER BY room_id", this::map);
    }

    private Room map(ResultSet resultSet) throws SQLException {
        return RoomFactory.create(
                resultSet.getString("room_id"),
                resultSet.getString("room_name"),
                resultSet.getInt("floor"),
                resultSet.getInt("capacity"),
                RoomType.fromCode(resultSet.getString("room_type")),
                RoomStatus.fromCode(resultSet.getString("status"))
        );
    }
}
