package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class BookingServiceImplTest {

    @Autowired
    private BookingService service;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setup() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@test.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Велосипед");
        item.setDescription("Горный велосипед");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);
    }

    @Test
    void testCreateBooking() {
        BookingDto dto = new BookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto result = service.create(booker.getId(), dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(result.getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    void testCreateBooking_withInvalidDates() {
        BookingDto dto = new BookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(2));
        dto.setEnd(LocalDateTime.now().plusDays(1));

        assertThrows(ResponseStatusException.class, () ->
                service.create(booker.getId(), dto));
    }

    @Test
    void testApproveBooking() {
        Booking booking = createBooking();

        BookingResponseDto result = service.approve(owner.getId(), booking.getId(), true);

        assertThat(result.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void testApproveBooking_byNonOwnerThrows() {
        Booking booking = createBooking();

        assertThrows(ResponseStatusException.class, () ->
                service.approve(booker.getId(), booking.getId(), true));
    }

    @Test
    void testGetBooking_asBookerAndOwner() {
        Booking booking = createBooking();

        BookingResponseDto result1 = service.get(booker.getId(), booking.getId());
        assertThat(result1.getId()).isEqualTo(booking.getId());

        BookingResponseDto result2 = service.get(owner.getId(), booking.getId());
        assertThat(result2.getId()).isEqualTo(booking.getId());
    }

    @Test
    void testGetBooking_asOtherUserThrows() {
        Booking booking = createBooking();

        User stranger = new User();
        stranger.setName("Stranger");
        stranger.setEmail("stranger@test.com");
        stranger = userRepository.save(stranger);

        Long strangerId = stranger.getId();
        assertThrows(ResponseStatusException.class, () ->
                service.get(strangerId, booking.getId()));
    }

    @Test
    void testGetAllByUserAndOwner() {
        createBooking();
        createBooking();

        List<BookingResponseDto> userBookings = service.getAllByUser(booker.getId(), BookingState.ALL);
        List<BookingResponseDto> ownerBookings = service.getAllByOwner(owner.getId(), BookingState.ALL);

        assertThat(userBookings).hasSize(2);
        assertThat(ownerBookings).hasSize(2);
    }

    private Booking createBooking() {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }
}
