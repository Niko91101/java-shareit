package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collections;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest request) {
        if (request == null) return null;

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestorId(request.getRequestor() != null ? request.getRequestor().getId() : null);
        dto.setCreated(request.getCreated());

        return dto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto dto) {
        if (dto == null) return null;

        ItemRequest request = new ItemRequest();
        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        request.setCreated(dto.getCreated());

        return request;
    }

    public static ItemRequestResponseDto toResponseDto(ItemRequest request) {
        if (request == null) return null;

        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());

        ItemRequestResponseDto.RequestorDto req = new ItemRequestResponseDto.RequestorDto();
        if (request.getRequestor() != null) {
            req.setId(request.getRequestor().getId());
            req.setName(request.getRequestor().getName());
        }
        dto.setRequestor(req);

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            dto.setItems(request.getItems().stream()
                    .map(item -> {
                        ItemRequestResponseDto.ItemForRequestDto itemDto =
                                new ItemRequestResponseDto.ItemForRequestDto();
                        itemDto.setId(item.getId());
                        itemDto.setName(item.getName());
                        itemDto.setOwnerId(item.getOwner() != null ? item.getOwner().getId() : null);
                        return itemDto;
                    })
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(Collections.emptyList());
        }

        return dto;
    }
}
