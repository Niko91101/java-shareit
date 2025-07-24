package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemResponseMapperTest {

    @Test
    void toResponseDto_returnsNullIfItemIsNull() {
        ItemResponseDto dto = ItemResponseMapper.toResponseDto(null, null, null, null);
        assertThat(dto).isNull();
    }

    @Test
    void toResponseDto_setsBasicFields() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemResponseDto dto = ItemResponseMapper.toResponseDto(item, null, null, null);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Item");
        assertThat(dto.getComments()).isEmpty();
    }

    @Test
    void toResponseDto_setsBookingsAndComments() {
        User user = new User();
        user.setId(99L);

        Booking lastBooking = new Booking();
        lastBooking.setId(5L);
        lastBooking.setBooker(user);
        lastBooking.setStart(LocalDateTime.now());
        lastBooking.setEnd(LocalDateTime.now().plusDays(1));

        Booking nextBooking = new Booking();
        nextBooking.setId(6L);
        nextBooking.setBooker(user);
        nextBooking.setStart(LocalDateTime.now().plusDays(2));
        nextBooking.setEnd(LocalDateTime.now().plusDays(3));

        Item item = new Item();
        item.setId(10L);
        item.setName("Bike");
        item.setDescription("Good");
        item.setAvailable(true);

        CommentDto comment = new CommentDto();
        comment.setId(50L);
        comment.setText("Nice");

        ItemResponseDto dto = ItemResponseMapper.toResponseDto(item, lastBooking, nextBooking, List.of(comment));

        assertThat(dto.getLastBooking()).isNotNull();
        assertThat(dto.getLastBooking().getId()).isEqualTo(5L);
        assertThat(dto.getNextBooking()).isNotNull();
        assertThat(dto.getNextBooking().getId()).isEqualTo(6L);
        assertThat(dto.getComments()).hasSize(1);
    }
}
