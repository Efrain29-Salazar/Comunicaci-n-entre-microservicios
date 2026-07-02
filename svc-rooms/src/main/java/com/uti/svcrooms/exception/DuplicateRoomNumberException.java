package com.uti.svcrooms.exception;

public class DuplicateRoomNumberException extends RuntimeException {
    public DuplicateRoomNumberException(String message) {
        super(message);
    }
}
