package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BookingServiceImplUnitTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl service;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");

        booker = new User();
        booker.setId(2L);
        booker.setName("Booker");

        item = new Item();
        item.setId(10L);
        item.setName("Test Item");
        item.setOwner(owner);
        item.setAvailable(true);

        booking = new Booking();
        booking.setId(100L);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(2));
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    void create_throwsIfUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        BookingDto dto = new BookingDto();
        dto.setItemId(10L);

        assertThrows(ResponseStatusException.class,
                () -> service.create(2L, dto));
    }

    @Test
    void create_throwsIfItemNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(10L)).thenReturn(Optional.empty());
        BookingDto dto = new BookingDto();
        dto.setItemId(10L);

        assertThrows(ResponseStatusException.class,
                () -> service.create(2L, dto));
    }

    @Test
    void approve_throwsIfBookingNotFound() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.approve(1L, 100L, true));
    }

    @Test
    void approve_throwsIfNotOwner() {
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        assertThrows(ResponseStatusException.class,
                () -> service.approve(999L, 100L, true));
    }

    @Test
    void approve_throwsIfAlreadyProcessed() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        assertThrows(ResponseStatusException.class,
                () -> service.approve(1L, 100L, true));
    }

    @Test
    void get_throwsIfNotBookerOrOwner() {
        User stranger = new User();
        stranger.setId(99L);

        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        assertThrows(ResponseStatusException.class,
                () -> service.get(stranger.getId(), 100L));
    }

    @Test
    void getAllByUser_returnsEmptyListIfNoBookings() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdOrderByStartDesc(2L))
                .thenReturn(Collections.emptyList());

        List<BookingResponseDto> result = service.getAllByUser(2L, BookingState.ALL);

        assertThat(result).isEmpty();
    }

    @Test
    void getAllByOwner_returnsEmptyListIfNoBookings() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(1L))
                .thenReturn(Collections.emptyList());

        List<BookingResponseDto> result = service.getAllByOwner(1L, BookingState.ALL);

        assertThat(result).isEmpty();
    }
}
