package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestResponseDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestBody ItemRequestDto dto) {
        log.info("Server: Создаём новый запрос '{}' от пользователя {}", dto.getDescription(), userId);
        return ResponseEntity.ok(itemRequestService.createRequest(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestResponseDto>> getOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Server: Получаем все запросы пользователя {}", userId);
        return ResponseEntity.ok(itemRequestService.getOwnRequests(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestResponseDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @RequestParam(defaultValue = "0") int from,
                                                               @RequestParam(defaultValue = "10") int size) {
        log.info("Server: Получаем все запросы других пользователей (userId={}, from={}, size={})",
                userId, from, size);
        return ResponseEntity.ok(itemRequestService.getAllRequests(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestResponseDto> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @PathVariable Long requestId) {
        log.info("Server: Получаем запрос {} (запрос от {})", requestId, userId);
        return ResponseEntity.ok(itemRequestService.getRequestById(userId, requestId));
    }
}
