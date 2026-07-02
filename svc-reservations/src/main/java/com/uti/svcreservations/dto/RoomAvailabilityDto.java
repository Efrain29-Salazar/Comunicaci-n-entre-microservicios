package com.uti.svcreservations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirrors the availability payload returned by svc-rooms
 * (GET /api/v1/rooms/{id}/availability).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityDto {
    private Long roomId;
    private boolean available;
    private Integer availableRooms;
}
