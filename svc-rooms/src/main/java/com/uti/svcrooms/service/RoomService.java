package com.uti.svcrooms.service;

import com.uti.svcrooms.dto.AvailabilityResponseDto;
import com.uti.svcrooms.dto.AvailabilityUpdateDto;
import com.uti.svcrooms.dto.RoomRequestDto;
import com.uti.svcrooms.dto.RoomResponseDto;

import java.util.List;

public interface RoomService {

    List<RoomResponseDto> getAllRooms();

    RoomResponseDto getRoomById(Long id);

    RoomResponseDto createRoom(RoomRequestDto requestDto);

    AvailabilityResponseDto getAvailability(Long id);

    AvailabilityResponseDto updateAvailability(Long id, AvailabilityUpdateDto updateDto);
}
