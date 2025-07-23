package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    @Test
    void toEntity_mapsDtoToEntity() {
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setId(2L);

        BookingDto dto = new BookingDto();
        dto.setItemId(2L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toEntity(dto, item, user);

        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(user);
        assertThat(booking.getStart()).isEqualTo(dto.getStart());
        assertThat(booking.getEnd()).isEqualTo(dto.getEnd());
    }

    @Test
    void toResponseDto_mapsEntityToResponseDto() {
        User booker = new User();
        booker.setId(3L);
        Item item = new Item();
        item.setId(4L);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(3));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);

        BookingResponseDto dto = BookingMapper.toResponseDto(booking);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getItem().getId()).isEqualTo(4L);
        assertThat(dto.getBooker().getId()).isEqualTo(3L);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void toDto_mapsBookingToDto() {
        User booker = new User();
        booker.setId(1L);
        Item item = new Item();
        item.setId(2L);

        Booking booking = new Booking();
        booking.setId(3L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto dto = BookingMapper.toDto(booking);

        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getItemId()).isEqualTo(2L);
        assertThat(dto.getBookerId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void toDto_returnsNullForNullBooking() {
        assertThat(BookingMapper.toDto(null)).isNull();
    }

    @Test
    void toEntity_returnsNullForNullDto() {
        assertThat(BookingMapper.toEntity(null, new Item(), new User())).isNull();
    }

    @Test
    void toResponseDto_returnsNullForNullBooking() {
        assertThat(BookingMapper.toResponseDto(null)).isNull();
    }
}
