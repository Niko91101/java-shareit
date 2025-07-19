package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest request) {
        if (request == null) return null;

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestor(request.getRequestor());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto dto) {
        if (dto == null) return null;

        ItemRequest request = new ItemRequest();
        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        request.setRequestor(dto.getRequestor());
        request.setCreated(dto.getCreated());
        return request;
    }
}
