package com.uti.svcreservations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityDto {
    private Long roomId;
    private boolean available;
    private Integer availableRooms;
}
