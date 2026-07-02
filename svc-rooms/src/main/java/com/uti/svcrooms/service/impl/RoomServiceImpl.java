package com.uti.svcrooms.service.impl;

import com.uti.svcrooms.dto.AvailabilityResponseDto;
import com.uti.svcrooms.dto.AvailabilityUpdateDto;
import com.uti.svcrooms.dto.RoomRequestDto;
import com.uti.svcrooms.dto.RoomResponseDto;
import com.uti.svcrooms.exception.DuplicateRoomNumberException;
import com.uti.svcrooms.exception.InvalidAvailabilityException;
import com.uti.svcrooms.exception.RoomNotFoundException;
import com.uti.svcrooms.mapper.RoomMapper;
import com.uti.svcrooms.model.Room;
import com.uti.svcrooms.repository.RoomRepository;
import com.uti.svcrooms.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public List<RoomResponseDto> getAllRooms() {
        log.info("Fetching all rooms");
        return roomRepository.findAll().stream()
                .map(roomMapper::toResponseDto)
                .toList();
    }

    @Override
    public RoomResponseDto getRoomById(Long id) {
        log.info("Fetching room with id {}", id);
        Room room = findRoomOrThrow(id);
        return roomMapper.toResponseDto(room);
    }

    @Override
    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto requestDto) {
        log.info("Creating room with roomNumber {}", requestDto.getRoomNumber());

        if (roomRepository.existsByRoomNumber(requestDto.getRoomNumber())) {
            throw new DuplicateRoomNumberException(
                    "Room number '" + requestDto.getRoomNumber() + "' already exists");
        }

        if (requestDto.getAvailableRooms() > requestDto.getTotalCapacity()) {
            throw new InvalidAvailabilityException("availableRooms cannot exceed totalCapacity");
        }

        Room room = roomMapper.toEntity(requestDto);
        Room saved = roomRepository.save(room);
        log.info("Room created with id {}", saved.getId());
        return roomMapper.toResponseDto(saved);
    }

    @Override
    public AvailabilityResponseDto getAvailability(Long id) {
        log.info("Checking availability for room id {}", id);
        Room room = findRoomOrThrow(id);
        return AvailabilityResponseDto.builder()
                .roomId(room.getId())
                .available(room.getAvailableRooms() > 0)
                .availableRooms(room.getAvailableRooms())
                .build();
    }

    @Override
    @Transactional
    public AvailabilityResponseDto updateAvailability(Long id, AvailabilityUpdateDto updateDto) {
        log.info("Updating availability for room id {} to {}", id, updateDto.getAvailableRooms());
        Room room = findRoomOrThrow(id);

        if (updateDto.getAvailableRooms() < 0) {
            throw new InvalidAvailabilityException("availableRooms cannot be negative");
        }
        if (updateDto.getAvailableRooms() > room.getTotalCapacity()) {
            throw new InvalidAvailabilityException("availableRooms cannot exceed totalCapacity");
        }

        room.setAvailableRooms(updateDto.getAvailableRooms());
        Room saved = roomRepository.save(room);

        return AvailabilityResponseDto.builder()
                .roomId(saved.getId())
                .available(saved.getAvailableRooms() > 0)
                .availableRooms(saved.getAvailableRooms())
                .build();
    }

    private Room findRoomOrThrow(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + id + " not found"));
    }
}
