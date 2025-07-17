package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId,
                           @RequestBody @Valid ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
                              @PathVariable @Positive Long itemId,
                              @RequestBody ItemDto itemDto) {

        return itemService.updateItem(ownerId, itemId, itemDto);
    }


    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                   @PathVariable @Positive Long itemId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) @Positive Long userId,
                                 @PathVariable @Positive Long itemId,
                                 @RequestBody @Valid CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}