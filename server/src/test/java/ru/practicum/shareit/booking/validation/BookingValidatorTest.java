package ru.practicum.shareit.booking.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingValidatorTest {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingValidator validator;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        validator = new BookingValidator(userRepository, itemRepository);
    }

    @Test
    void checkItemAvailability_throwsIfItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> validator.checkItemAvailability(1L));
    }

    @Test
    void checkItemAvailability_throwsIfNotAvailable() {
        Item item = new Item();
        item.setAvailable(false);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ResponseStatusException.class, () -> validator.checkItemAvailability(1L));
    }

    @Test
    void checkItemAvailability_passesIfAvailable() {
        Item item = new Item();
        item.setAvailable(true);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertDoesNotThrow(() -> validator.checkItemAvailability(1L));
    }

    @Test
    void checkOwnerIsNotBooker_throwsIfOwnerMatches() {
        Item item = new Item();
        User owner = new User();
        owner.setId(5L);
        item.setOwner(owner);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ResponseStatusException.class, () -> validator.checkOwnerIsNotBooker(1L, 5L));
    }

    @Test
    void checkOwnerIsNotBooker_passesIfDifferentUser() {
        Item item = new Item();
        User owner = new User();
        owner.setId(5L);
        item.setOwner(owner);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertDoesNotThrow(() -> validator.checkOwnerIsNotBooker(1L, 99L));
    }

    @Test
    void checkBookingDates_throwsIfNullDates() {
        assertThrows(ResponseStatusException.class, () -> validator.checkBookingDates(null, LocalDateTime.now()));
        assertThrows(ResponseStatusException.class, () -> validator.checkBookingDates(LocalDateTime.now(), null));
    }

    @Test
    void checkBookingDates_throwsIfEndNotAfterStart() {
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        assertThrows(ResponseStatusException.class, () -> validator.checkBookingDates(now, now));
    }

    @Test
    void checkBookingDates_throwsIfStartInPast() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        assertThrows(ResponseStatusException.class, () -> validator.checkBookingDates(start, end));
    }

    @Test
    void checkBookingDates_passesIfValid() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);
        assertDoesNotThrow(() -> validator.checkBookingDates(start, end));
    }
}
