package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ErrorHandlerUnitTest {


    @Test
    void handleResponseStatus_returnsCustomStatus() {
        ErrorHandler handler = new ErrorHandler();
        ResponseStatusException ex = new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not found");

        ResponseEntity<Map<String, String>> response = handler.handleResponseStatus(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).containsEntry("error", "Not found");
    }

    @Test
    void handleEntityNotFound_returnsCustomMessage() {
        ErrorHandler handler = new ErrorHandler();

        jakarta.persistence.EntityNotFoundException ex = new jakarta.persistence.EntityNotFoundException("Не найдено");

        Map<String, String> response = handler.handleEntityNotFound(ex);

        assertThat(response.get("error")).isEqualTo("Не найдено");
    }

    @Test
    void handleEntityNotFound_returnsDefaultMessageWhenNull() {
        ErrorHandler handler = new ErrorHandler();

        jakarta.persistence.EntityNotFoundException ex = new jakarta.persistence.EntityNotFoundException();

        Map<String, String> response = handler.handleEntityNotFound(ex);

        assertThat(response.get("error")).isEqualTo("Объект не найден");
    }
}
