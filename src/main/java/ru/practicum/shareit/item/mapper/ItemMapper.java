package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapper {

    public ItemDto toDto(Item item) {
        if (item == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        if (item.getOwner() != null) dto.setOwnerId(item.getOwner().getId());
        if (item.getRequest() != null) dto.setRequestId(item.getRequest().getId());
        return dto;
    }

    public Item toItem(ItemDto dto) {
        if (dto == null) return null;
        Item item = new Item();

        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }
}
