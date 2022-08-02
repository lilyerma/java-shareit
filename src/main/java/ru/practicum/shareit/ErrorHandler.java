package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    // 400 — если ошибка валидации: ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleNotCorrectValidate(ValidationException exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 404 — для всех ситуаций, если искомый объект не найден
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 500 - для ситуаций конфликта существующих значений
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleNotFoundException(ConflictException exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }


}
