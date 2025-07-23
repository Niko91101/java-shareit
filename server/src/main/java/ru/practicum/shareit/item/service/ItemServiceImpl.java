package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemResponseMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemDto addItem(Long ownerId, ItemDto itemDto) {
        // Убираем валидацию ownerId, так как это уже сделано на шлюзе
        Item item = ItemMapper.toItem(itemDto);

        item.setOwner(userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден")));

        if (itemDto.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрос не найден")));
        }

        Item savedItem = itemRepository.save(item);
        return ItemMapper.toDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        // Валидация ownerId теперь не нужна, так как проверка уже происходит на шлюзе

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Предмет с таким ID не найден"));

        // Проверяем, что пользователь является владельцем предмета
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Вы не являетесь владельцем этого предмета");
        }

        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        Item savedItem = itemRepository.save(item);

        return ItemMapper.toDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Предмет не найден"));

        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());

        return ItemResponseMapper.toResponseDto(item, null, null, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsByOwner(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);

        return items.stream()
                .map(item -> ItemResponseMapper.toResponseDto(item, null, null, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.searchAvailableItems(text)
                .stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        // Здесь также валидация уже не нужна, так как она происходит на шлюзе
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь не найдена"));

        boolean isBooker = bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now());
        if (!isBooker) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь не арендовал эту вещь или аренда ещё не завершена");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден")));
        comment.setCreated(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return CommentMapper.toDto(saved);
    }
}


