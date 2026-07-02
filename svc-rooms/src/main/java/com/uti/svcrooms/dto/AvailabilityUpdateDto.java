package com.uti.svcrooms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityUpdateDto {

    @NotNull(message = "availableRooms is required")
    private Integer availableRooms;
}
