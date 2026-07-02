package com.uti.svcreservations.client;

import com.uti.svcreservations.dto.RoomAvailabilityDto;
import com.uti.svcreservations.dto.RoomDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Synchronous client to svc-rooms using RestTemplate.
 * Used for: checking availability on reservation creation, and enriching
 * getReservationById() / checkout() responses.
 * Protected by Resilience4j Circuit Breaker + Retry, both configured under
 * the "roomsService" instance name (see application.yml).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomsRestTemplateClient {

    private final RestTemplate restTemplate;

    @Value("${rooms.service.url}")
    private String roomsServiceUrl;

    @CircuitBreaker(name = "roomsService", fallbackMethod = "fallbackAvailability")
    @Retry(name = "roomsService")
    public RoomAvailabilityDto checkAvailability(Long roomId) {
        log.info("Checking availability via RestTemplate for room {}", roomId);
        String url = roomsServiceUrl + "/api/v1/rooms/" + roomId + "/availability";
        return restTemplate.getForObject(url, RoomAvailabilityDto.class);
    }

    @CircuitBreaker(name = "roomsService", fallbackMethod = "fallbackRoom")
    @Retry(name = "roomsService")
    public RoomDto getRoom(Long roomId) {
        log.info("Fetching room via RestTemplate for room {}", roomId);
        String url = roomsServiceUrl + "/api/v1/rooms/" + roomId;
        return restTemplate.getForObject(url, RoomDto.class);
    }

    /**
     * Triggered when svc-rooms is unreachable (or the circuit is open) while
     * checking availability. Returns null so the service layer can react
     * loudly (creation must fail with 503 - it cannot verify availability).
     */
    private RoomAvailabilityDto fallbackAvailability(Long roomId, Throwable throwable) {
        log.warn("Fallback triggered for checkAvailability(roomId={}): {}", roomId, throwable.getMessage());
        return null;
    }

    /**
     * Triggered when svc-rooms is unreachable while fetching room data.
     * Returns null so the service layer can degrade gracefully with a
     * placeholder message instead of failing the whole request.
     */
    private RoomDto fallbackRoom(Long roomId, Throwable throwable) {
        log.warn("Fallback triggered for getRoom(roomId={}): {}", roomId, throwable.getMessage());
        return null;
    }
}
