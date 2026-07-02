package com.uti.svcreservations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirrors the room data returned by svc-rooms (GET /api/v1/rooms/{id}).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private String roomNumber;
    private String type;
    private Double pricePerNight;
    private Integer totalCapacity;
    private Integer availableRooms;
    private Integer floor;
    private String description;
}
