package ru.practicum.shareit.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GatewayErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(GatewayErrorHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Ошибка валидации");
        error.put("details", ex.getBindingResult().getFieldError().getDefaultMessage());
        log.warn("Ошибка валидации: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Некорректный параметр");
        error.put("details", ex.getMessage());
        log.warn("Некорректный параметр: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getReason() != null ? ex.getReason() : "Ошибка");
        log.error("Ошибка с кодом {}: {}", ex.getStatusCode(), ex.getReason());
        return ResponseEntity.status(ex.getStatusCode())
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(error);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleOther(Throwable ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Внутренняя ошибка сервера");
        error.put("details", ex.getMessage());
        log.error("Необработанное исключение", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(error);
    }
}
