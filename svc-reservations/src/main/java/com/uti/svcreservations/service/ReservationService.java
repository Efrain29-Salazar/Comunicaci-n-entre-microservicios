package com.uti.svcreservations.service;

import com.uti.svcreservations.dto.ReservationRequestDto;
import com.uti.svcreservations.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {

    ReservationResponseDto createReservation(ReservationRequestDto requestDto);

    ReservationResponseDto getReservationById(Long id);

    List<ReservationResponseDto> getReservationsByEmail(String email);

    ReservationResponseDto checkout(Long id);
}
