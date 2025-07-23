package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @RequestBody ItemDto itemDto) {
        log.info("Server: Создаём вещь {} для пользователя {}", itemDto, ownerId);
        return ResponseEntity.ok(itemService.addItem(ownerId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @PathVariable Long itemId,
                                          @RequestBody ItemDto itemDto) {
        log.info("Server: Обновляем вещь {} (владелец {})", itemId, ownerId);
        return ResponseEntity.ok(itemService.updateItem(ownerId, itemId, itemDto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @PathVariable Long itemId) {
        log.info("Server: Получаем вещь {} (запрос от {})", itemId, userId);
        return ResponseEntity.ok(itemService.getItem(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Server: Получаем все вещи владельца {}", ownerId);
        return ResponseEntity.ok(itemService.getItemsByOwner(ownerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text) {
        log.info("Server: Поиск вещей по тексту '{}'", text);
        return ResponseEntity.ok(itemService.search(text));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long itemId,
                                                 @RequestBody CommentDto commentDto) {
        log.info("Server:Добавляем комментарий к вещи id={}, пользователь={}, текст='{}'", itemId, userId, commentDto.getText());
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addComment(userId, itemId, commentDto));
    }
}
