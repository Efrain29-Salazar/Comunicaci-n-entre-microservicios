package com.uti.svcreservations.service.impl;

import com.uti.svcreservations.client.RoomsRestTemplateClient;
import com.uti.svcreservations.client.RoomsWebClient;
import com.uti.svcreservations.dto.ReservationRequestDto;
import com.uti.svcreservations.dto.ReservationResponseDto;
import com.uti.svcreservations.dto.RoomAvailabilityDto;
import com.uti.svcreservations.dto.RoomDto;
import com.uti.svcreservations.exception.DuplicateReservationException;
import com.uti.svcreservations.exception.InvalidCheckoutException;
import com.uti.svcreservations.exception.InvalidDateRangeException;
import com.uti.svcreservations.exception.ReservationNotFoundException;
import com.uti.svcreservations.exception.RoomNotAvailableException;
import com.uti.svcreservations.exception.RoomServiceUnavailableException;
import com.uti.svcreservations.mapper.ReservationMapper;
import com.uti.svcreservations.model.Reservation;
import com.uti.svcreservations.model.ReservationStatus;
import com.uti.svcreservations.repository.ReservationRepository;
import com.uti.svcreservations.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Orchestrates reservation business rules and the calls to svc-rooms.
 * - createReservation() uses RestTemplate (availability) + WebClient (room data).
 * - getReservationById() / checkout() use RestTemplate to enrich the response.
 * - getReservationsByEmail() uses WebClient to enrich each item.
 * If svc-rooms is completely down, the client fallback methods return null:
 * creation fails loudly with 503, while read operations degrade gracefully
 * and still return 200 OK with a placeholder message.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final String ROOM_INFO_UNAVAILABLE = "Room information temporarily unavailable";

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final RoomsRestTemplateClient roomsRestTemplateClient;
    private final RoomsWebClient roomsWebClient;

    @Override
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        log.info("Creating reservation for guest {} and room {}", requestDto.getGuestEmail(), requestDto.getRoomId());

        if (!requestDto.getCheckOutDate().isAfter(requestDto.getCheckInDate())) {
            throw new InvalidDateRangeException("checkOutDate must be after checkInDate");
        }

        reservationRepository
                .findByGuestEmailAndRoomIdAndStatus(requestDto.getGuestEmail(), requestDto.getRoomId(), ReservationStatus.ACTIVE)
                .ifPresent(r -> {
                    throw new DuplicateReservationException(
                            "Guest already has an ACTIVE reservation for this room");
                });

        RoomAvailabilityDto availability = roomsRestTemplateClient.checkAvailability(requestDto.getRoomId());
        if (availability == null) {
            log.warn("svc-rooms unavailable while checking availability for room {}", requestDto.getRoomId());
            throw new RoomServiceUnavailableException(ROOM_INFO_UNAVAILABLE);
        }
        if (!availability.isAvailable()) {
            throw new RoomNotAvailableException("Room is not available");
        }

        RoomDto room = roomsWebClient.getRoom(requestDto.getRoomId());
        if (room == null) {
            log.warn("svc-rooms unavailable while fetching room data for room {}", requestDto.getRoomId());
            throw new RoomServiceUnavailableException(ROOM_INFO_UNAVAILABLE);
        }

        Reservation reservation = reservationMapper.toEntity(requestDto);
        Reservation saved = reservationRepository.save(reservation);
        log.info("Reservation created with id {}", saved.getId());

        return reservationMapper.toResponseDto(saved, room);
    }

    @Override
    public ReservationResponseDto getReservationById(Long id) {
        log.info("Fetching reservation with id {}", id);
        Reservation reservation = findReservationOrThrow(id);
        RoomDto room = roomsRestTemplateClient.getRoom(reservation.getRoomId());
        if (room == null) {
            log.warn("svc-rooms unavailable while enriching reservation {}", id);
        }
        return reservationMapper.toResponseDto(reservation, room);
    }

    @Override
    public List<ReservationResponseDto> getReservationsByEmail(String email) {
        log.info("Fetching reservations for guest {}", email);
        List<Reservation> reservations = reservationRepository.findByGuestEmail(email);
        return reservations.stream()
                .map(reservation -> {
                    RoomDto room = roomsWebClient.getRoom(reservation.getRoomId());
                    return reservationMapper.toResponseDto(reservation, room);
                })
                .toList();
    }

    @Override
    @Transactional
    public ReservationResponseDto checkout(Long id) {
        log.info("Processing checkout for reservation {}", id);
        Reservation reservation = findReservationOrThrow(id);

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new InvalidCheckoutException("Only ACTIVE reservations can be checked out");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        Reservation saved = reservationRepository.save(reservation);

        RoomDto room = roomsRestTemplateClient.getRoom(saved.getRoomId());
        if (room == null) {
            log.warn("svc-rooms unavailable while enriching checkout response for reservation {}", id);
        }
        return reservationMapper.toResponseDto(saved, room);
    }

    private Reservation findReservationOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation with id " + id + " not found"));
    }
}
