package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ArgumentException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Component
public class ErrorHandler {

    @ExceptionHandler(ArgumentException.class)
    public ResponseEntity<Map<String,String>> handleNotCorrectInput(ArgumentException ex) {
        Map<String,String> resp = new HashMap<>();
        resp.put("error", ex.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    // 400 — если ошибка валидации: ConstraintViolationException
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handlePathVariable(ConstraintViolationException exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

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
