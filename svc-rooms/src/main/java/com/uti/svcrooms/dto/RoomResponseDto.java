package com.uti.svcrooms.dto;

import com.uti.svcrooms.model.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Public representation of a Room returned by the API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {
    private Long id;
    private String roomNumber;
    private RoomType type;
    private Double pricePerNight;
    private Integer totalCapacity;
    private Integer availableRooms;
    private Integer floor;
    private String description;
}
