package com.uti.svcreservations.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uti.svcreservations.model.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    private Long id;
    private Long roomId;
    private String guestName;
    private String guestEmail;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    private Integer totalNights;
    private ReservationStatus status;
    private LocalDateTime createdAt;

    private String roomNumber;
    private String roomType;
    private Double pricePerNight;
}
