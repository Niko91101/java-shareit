package ru.practicum.shareit.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    private ResponseEntity<Map<String, String>> buildError(HttpStatus status, String message, String details) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        if (details != null) {
            error.put("details", details);
        }
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException ex) {
        return buildError(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason(), null);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEntityNotFound(EntityNotFoundException ex) {
        return Map.of("error", ex.getMessage() != null ? ex.getMessage() : "Объект не найден");
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, String>> handleOtherErrors(Throwable ex) {
        log.error("Необработанная ошибка на сервере", ex);

        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка обработки запроса", ex.getMessage());
    }
}

