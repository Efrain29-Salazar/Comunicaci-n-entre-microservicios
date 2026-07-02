package com.uti.svcreservations.repository;

import com.uti.svcreservations.model.Reservation;
import com.uti.svcreservations.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByGuestEmail(String guestEmail);

    Optional<Reservation> findByGuestEmailAndRoomIdAndStatus(
            String guestEmail, Long roomId, ReservationStatus status);
}
