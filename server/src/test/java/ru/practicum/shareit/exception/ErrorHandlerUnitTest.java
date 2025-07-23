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
    void handleValidationError_returnsBadRequest() {
        ErrorHandler handler = new ErrorHandler();

        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "Invalid value");
        when(bindingResult.getFieldError()).thenReturn(fieldError);

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationError(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("error", "Ошибка валидации");
        assertThat(response.getBody()).containsKey("details");
    }

    @Test
    void handleTypeMismatch_returnsBadRequest() {
        ErrorHandler handler = new ErrorHandler();
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getMessage()).thenReturn("Bad param");

        ResponseEntity<Map<String, String>> response = handler.handleTypeMismatch(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("error", "Некорректный параметр запроса");
        assertThat(response.getBody()).containsEntry("details", "Bad param");
    }

    @Test
    void handleResponseStatus_returnsCustomStatus() {
        ErrorHandler handler = new ErrorHandler();
        ResponseStatusException ex = new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not found");

        ResponseEntity<Map<String, String>> response = handler.handleResponseStatus(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).containsEntry("error", "Not found");
    }

    @Test
    void handleOtherErrors_returnsBadRequestInsteadOf500() {
        ErrorHandler handler = new ErrorHandler();
        Throwable ex = new RuntimeException("boom");

        ResponseEntity<Map<String, String>> response = handler.handleOtherErrors(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("error", "Ошибка обработки запроса");
        assertThat(response.getBody()).containsEntry("details", "boom");
    }

    @Test
    void handleValidationError_handlesNullFieldError() {
        ErrorHandler handler = new ErrorHandler();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldError()).thenReturn(null);

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationError(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("error", "Ошибка валидации");
        assertThat(response.getBody().get("details")).isEqualTo("Invalid data");
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
