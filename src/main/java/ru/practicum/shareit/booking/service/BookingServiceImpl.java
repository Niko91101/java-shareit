package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.booking.validation.BookingValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingValidator bookingValidator;

    @Override
    @Transactional
    public BookingResponseDto create(Long userId, BookingDto bookingDto) {
        bookingValidator.checkUserExistence(userId);
        bookingValidator.checkItemAvailability(bookingDto.getItemId());
        bookingValidator.checkOwnerIsNotBooker(bookingDto.getItemId(), userId);
        bookingValidator.checkBookingDates(bookingDto.getStart(), bookingDto.getEnd());

        User booker = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь не найдена"));

        Booking booking = BookingMapper.toEntity(bookingDto, item, booker);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);

        return BookingMapper.toResponseDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto approve(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Только владелец может подтверждать/отклонять бронирование");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Бронирование уже обработано");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto get(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Бронирование не найдено"));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет доступа к бронированию");
        }

        return BookingMapper.toResponseDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllByUser(Long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
        return filterBookingsByState(bookings, state).stream()
                .map(BookingMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllByOwner(Long ownerId, BookingState state) {
        userRepository.findById(ownerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        List<Booking> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
        return filterBookingsByState(bookings, state).stream()
                .map(BookingMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    private List<Booking> filterBookingsByState(List<Booking> bookings, BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        return bookings.stream()
                .filter(booking -> {
                    return switch (state) {
                        case ALL -> true;
                        case CURRENT -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
                        case PAST -> booking.getEnd().isBefore(now);
                        case FUTURE -> booking.getStart().isAfter(now);
                        case WAITING -> booking.getStatus() == BookingStatus.WAITING;
                        case REJECTED -> booking.getStatus() == BookingStatus.REJECTED;
                        default -> false;
                    };
                })
                .collect(Collectors.toList());
    }
}

