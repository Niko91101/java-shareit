package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemResponseMapper {
    public ItemResponseDto toResponseDto(Item item, Booking lastBooking, Booking nextBooking, List<CommentDto> comments) {
        if (item == null) return null;
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) dto.setRequestId(item.getRequest().getId());

        if (lastBooking != null) {
            BookingShortDto last = new BookingShortDto();
            last.setId(lastBooking.getId());
            last.setBookerId(lastBooking.getBooker().getId());
            dto.setLastBooking(last);
        }
        if (nextBooking != null) {
            BookingShortDto next = new BookingShortDto();
            next.setId(nextBooking.getId());
            next.setBookerId(nextBooking.getBooker().getId());
            dto.setNextBooking(next);
        }
        dto.setComments(comments);
        return dto;
    }
}
