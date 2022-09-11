package ru.practicum.shareit.exception;

public class ArgumentException extends RuntimeException {

    public ArgumentException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

