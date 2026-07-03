package com.uti.svcreservations.client;

import com.uti.svcreservations.dto.RoomDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomsWebClient {

    private final WebClient webClient;

    @Value("${rooms.service.url}")
    private String roomsServiceUrl;

    @CircuitBreaker(name = "roomsService", fallbackMethod = "fallbackRoom")
    @Retry(name = "roomsService")
    public RoomDto getRoom(Long roomId) {
        log.info("Fetching room via WebClient for room {}", roomId);
        return webClient.get()
                .uri(roomsServiceUrl + "/api/v1/rooms/" + roomId)
                .retrieve()
                .bodyToMono(RoomDto.class)
                .block();
    }

    private RoomDto fallbackRoom(Long roomId, Throwable throwable) {
        log.warn("Fallback triggered for WebClient getRoom(roomId={}): {}", roomId, throwable.getMessage());
        return null;
    }
}
