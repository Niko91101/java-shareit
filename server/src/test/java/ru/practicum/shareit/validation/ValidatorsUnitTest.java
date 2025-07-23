package ru.practicum.shareit.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.validation.BookingValidator;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.item.validation.ItemValidator;
import ru.practicum.shareit.user.validation.UserValidator;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ValidatorsUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    private UserValidator userValidator;
    private ItemValidator itemValidator;
    private BookingValidator bookingValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userValidator = new UserValidator();
        itemValidator = new ItemValidator(itemRepository, userRepository);
        bookingValidator = new BookingValidator(userRepository, itemRepository);
    }


    @Test
    void userValidator_throwsIfEmailNotUnique() {
        when(userRepository.existsByEmail("exists@test.com")).thenReturn(true);
        assertThrows(ResponseStatusException.class,
                () -> userValidator.checkEmailUnique("exists@test.com", userRepository));
    }

    @Test
    void itemValidator_throwsIfItemNotFound() {
        when(itemRepository.existsById(100L)).thenReturn(false);
        assertThrows(ResponseStatusException.class,
                () -> itemValidator.checkItemExistence(100L));
    }

    @Test
    void bookingValidator_throwsIfDatesInvalid() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        assertThrows(ResponseStatusException.class,
                () -> bookingValidator.checkBookingDates(start, end));
    }
}
