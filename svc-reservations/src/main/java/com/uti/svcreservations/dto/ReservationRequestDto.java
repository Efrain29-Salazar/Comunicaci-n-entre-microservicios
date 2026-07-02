package com.uti.svcreservations.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {

    @NotNull(message = "roomId is required")
    private Long roomId;

    @NotBlank(message = "guestName is required")
    private String guestName;

    @NotBlank(message = "guestEmail is required")
    @Email(message = "guestEmail must be a valid email")
    private String guestEmail;

    @NotNull(message = "checkInDate is required")
    private LocalDate checkInDate;

    @NotNull(message = "checkOutDate is required")
    private LocalDate checkOutDate;
}
