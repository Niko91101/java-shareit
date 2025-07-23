package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookItemRequestDto {
    @NotNull
    private Long itemId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}
