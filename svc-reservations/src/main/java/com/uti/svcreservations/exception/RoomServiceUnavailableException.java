package com.uti.svcreservations.exception;

public class RoomServiceUnavailableException extends RuntimeException {
    public RoomServiceUnavailableException(String message) {
        super(message);
    }
}
