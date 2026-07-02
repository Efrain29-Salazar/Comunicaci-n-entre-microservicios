package com.uti.svcrooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponseDto {
    private Long roomId;
    private boolean available;
    private Integer availableRooms;
}
