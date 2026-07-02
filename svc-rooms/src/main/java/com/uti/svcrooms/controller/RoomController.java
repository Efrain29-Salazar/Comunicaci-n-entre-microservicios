package com.uti.svcrooms.controller;

import com.uti.svcrooms.dto.AvailabilityResponseDto;
import com.uti.svcrooms.dto.AvailabilityUpdateDto;
import com.uti.svcrooms.dto.RoomRequestDto;
import com.uti.svcrooms.dto.RoomResponseDto;
import com.uti.svcrooms.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing the room catalog and availability of Hotel Los Andes.
 * No try/catch here on purpose: errors are handled by GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@Valid @RequestBody RoomRequestDto requestDto) {
        RoomResponseDto created = roomService.createRoom(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<AvailabilityResponseDto> getAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getAvailability(id));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<AvailabilityResponseDto> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityUpdateDto updateDto) {
        return ResponseEntity.ok(roomService.updateAvailability(id, updateDto));
    }
}
