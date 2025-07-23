package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestBody BookingDto bookingDto) {
        log.info("Server: Создаю бронирование {} от пользователя {}", bookingDto, userId);
        return ResponseEntity.ok(bookingService.create(userId, bookingDto));
    }

    @PatchMapping("/{bookingId:[0-9]+}")
    public ResponseEntity<BookingResponseDto> approve(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                      @PathVariable Long bookingId,
                                                      @RequestParam boolean approved) {
        log.info("Server: Подтверждение бронирования {} от владельца {} (approved={})",
                bookingId, ownerId, approved);
        return ResponseEntity.ok(bookingService.approve(ownerId, bookingId, approved));
    }

    @GetMapping("/{bookingId:[0-9]+}")
    public ResponseEntity<BookingResponseDto> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable Long bookingId) {
        log.info("Server: Получаем бронирование {} (запрос от {})", bookingId, userId);
        return ResponseEntity.ok(bookingService.get(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                              @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Server: Получаем все бронирования пользователя {} с фильтром {}", userId, state);
        return ResponseEntity.ok(bookingService.getAllByUser(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> getByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                               @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Server: Получаем все бронирования для вещей владельца {} с фильтром {}", ownerId, state);
        return ResponseEntity.ok(bookingService.getAllByOwner(ownerId, state));
    }
}
