package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CommentDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.info("Создаём новую вещь: {}, пользователь={}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Обновляем вещь с id={}, пользователь={}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId) {
        log.info("Получаем вещь с id={}, пользователь={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получаем все вещи пользователя={}, from={}, size={}", userId, from, size);
        return itemClient.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Ищем вещи по запросу='{}', пользователь={}, from={}, size={}", text, userId, from, size);
        return itemClient.searchItems(userId, text, from, size);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId) {
        log.info("Удаляем вещь с id={}, пользователь={}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        log.info("Добавляем комментарий к вещи id={}, пользователь={}, текст='{}'",
                itemId, userId, commentDto.getText());
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
