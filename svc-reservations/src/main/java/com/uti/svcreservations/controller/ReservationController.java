package com.uti.svcreservations.controller;

import com.uti.svcreservations.dto.ReservationRequestDto;
import com.uti.svcreservations.dto.ReservationResponseDto;
import com.uti.svcreservations.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing reservation operations.
 * No try/catch here on purpose: errors are handled by GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto created = reservationService.createReservation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping("/guest/{email}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(reservationService.getReservationsByEmail(email));
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<ReservationResponseDto> checkout(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.checkout(id));
    }
}
