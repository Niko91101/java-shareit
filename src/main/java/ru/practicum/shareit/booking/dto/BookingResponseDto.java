package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private BookerShortDto booker;
    private ItemShortDto item;

    @Data
    @NoArgsConstructor
    public static class BookerShortDto {
        private Long id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    public static class ItemShortDto {
        private Long id;
        private String name;
    }
}
