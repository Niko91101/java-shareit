package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Long ownerId, ItemDto itemDto);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    ItemDto getItem(Long itemId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    List<ItemDto> search(String text);
}
