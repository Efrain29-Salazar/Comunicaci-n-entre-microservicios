package com.uti.svcreservations.mapper;

import com.uti.svcreservations.dto.ReservationRequestDto;
import com.uti.svcreservations.dto.ReservationResponseDto;
import com.uti.svcreservations.dto.RoomDto;
import com.uti.svcreservations.model.Reservation;
import com.uti.svcreservations.model.ReservationStatus;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class ReservationMapper {

    private static final String ROOM_INFO_UNAVAILABLE = "Room information temporarily unavailable";

    public Reservation toEntity(ReservationRequestDto dto) {
        int nights = (int) ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        return Reservation.builder()
                .roomId(dto.getRoomId())
                .guestName(dto.getGuestName())
                .guestEmail(dto.getGuestEmail())
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .totalNights(nights)
                .status(ReservationStatus.ACTIVE)
                .build();
    }

    public ReservationResponseDto toResponseDto(Reservation reservation, RoomDto room) {
        ReservationResponseDto.ReservationResponseDtoBuilder builder = ReservationResponseDto.builder()
                .id(reservation.getId())
                .roomId(reservation.getRoomId())
                .guestName(reservation.getGuestName())
                .guestEmail(reservation.getGuestEmail())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .totalNights(reservation.getTotalNights())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt());

        if (room != null) {
            builder.roomNumber(room.getRoomNumber())
                    .roomType(room.getType())
                    .pricePerNight(room.getPricePerNight());
        } else {
            builder.roomNumber(ROOM_INFO_UNAVAILABLE)
                    .roomType(null)
                    .pricePerNight(null);
        }

        return builder.build();
    }
}
