package ru.practicum.shareit.exception;


public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}


