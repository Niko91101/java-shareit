package ru.practicum.shareit.booking.dto;

import java.util.Arrays;
import java.util.Optional;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<BookingState> from(String stateParam) {
        return Arrays.stream(values())
                .filter(state -> state.name().equalsIgnoreCase(stateParam))
                .findFirst();
    }
}
