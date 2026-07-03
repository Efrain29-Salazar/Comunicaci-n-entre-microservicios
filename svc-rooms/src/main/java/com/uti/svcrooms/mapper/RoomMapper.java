package com.uti.svcrooms.mapper;

import com.uti.svcrooms.dto.RoomRequestDto;
import com.uti.svcrooms.dto.RoomResponseDto;
import com.uti.svcrooms.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room toEntity(RoomRequestDto dto) {
        return Room.builder()
                .roomNumber(dto.getRoomNumber())
                .type(dto.getType())
                .pricePerNight(dto.getPricePerNight())
                .totalCapacity(dto.getTotalCapacity())
                .availableRooms(dto.getAvailableRooms())
                .floor(dto.getFloor())
                .description(dto.getDescription())
                .build();
    }

    public RoomResponseDto toResponseDto(Room room) {
        return RoomResponseDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .type(room.getType())
                .pricePerNight(room.getPricePerNight())
                .totalCapacity(room.getTotalCapacity())
                .availableRooms(room.getAvailableRooms())
                .floor(room.getFloor())
                .description(room.getDescription())
                .build();
    }
}
