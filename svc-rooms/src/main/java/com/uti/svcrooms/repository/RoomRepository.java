package com.uti.svcrooms.repository;

import com.uti.svcrooms.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomNumber(String roomNumber);

    Optional<Room> findByRoomNumber(String roomNumber);
}
