package com.uti.svcrooms.dto;

import com.uti.svcrooms.model.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload used to register a new room. Kept fully separate from the JPA entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDto {

    @NotBlank(message = "roomNumber is required")
    private String roomNumber;

    @NotNull(message = "type is required")
    private RoomType type;

    @NotNull(message = "pricePerNight is required")
    @Min(value = 0, message = "pricePerNight must be positive")
    private Double pricePerNight;

    @NotNull(message = "totalCapacity is required")
    @Min(value = 1, message = "totalCapacity must be at least 1")
    private Integer totalCapacity;

    @NotNull(message = "availableRooms is required")
    @Min(value = 0, message = "availableRooms cannot be negative")
    private Integer availableRooms;

    private Integer floor;

    private String description;
}
