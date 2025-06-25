package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@Data
@NoArgsConstructor
public class Booking {

    private Long id;
    private Instant start;
    private Instant end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
