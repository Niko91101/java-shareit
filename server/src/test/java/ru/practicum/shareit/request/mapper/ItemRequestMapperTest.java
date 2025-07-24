package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    @Test
    void toItemRequest_mapsDtoToEntity() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Нужен велосипед");

        ItemRequest entity = ItemRequestMapper.toItemRequest(dto);

        assertThat(entity.getDescription()).isEqualTo("Нужен велосипед");
        assertThat(entity.getCreated()).isNull();
        assertThat(entity.getRequestor()).isNull();
    }

    @Test
    void toResponseDto_mapsEntityToDto() {
        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("Стас");

        ItemRequest request = new ItemRequest();
        request.setId(5L);
        request.setDescription("Ищу ноутбук");
        request.setCreated(LocalDateTime.of(2025, 7, 23, 10, 0));
        request.setRequestor(requestor);

        ItemRequestResponseDto dto = ItemRequestMapper.toResponseDto(request);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getDescription()).isEqualTo("Ищу ноутбук");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 10, 0));
        assertThat(dto.getRequestor().getId()).isEqualTo(1L);
        assertThat(dto.getRequestor().getName()).isEqualTo("Стас");
    }

    @Test
    void toItemRequest_setsFieldsCorrectly() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Хочу ноутбук");

        ItemRequest result = ItemRequestMapper.toItemRequest(dto);

        assertThat(result.getDescription()).isEqualTo("Хочу ноутбук");
        assertThat(result.getCreated()).isNull();
        assertThat(result.getRequestor()).isNull();
    }

    @Test
    void toResponseDto_setsAllFieldsCorrectly() {
        User user = new User();
        user.setId(10L);
        user.setName("Анна");

        ItemRequest request = new ItemRequest();
        request.setId(5L);
        request.setDescription("Нужен велосипед");
        request.setCreated(LocalDateTime.of(2025, 7, 23, 15, 0));
        request.setRequestor(user);

        ItemRequestResponseDto dto = ItemRequestMapper.toResponseDto(request);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getDescription()).isEqualTo("Нужен велосипед");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 15, 0));
        assertThat(dto.getRequestor().getId()).isEqualTo(10L);
        assertThat(dto.getRequestor().getName()).isEqualTo("Анна");
    }

    @Test
    void toResponseDto_handlesNullRequestorAndItems() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Без владельца");
        request.setCreated(LocalDateTime.of(2025, 7, 20, 12, 0));
        request.setRequestor(null);

        ItemRequestResponseDto dto = ItemRequestMapper.toResponseDto(request);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Без владельца");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 20, 12, 0));

        assertThat(dto.getRequestor()).isNotNull();
        assertThat(dto.getRequestor().getId()).isNull();
        assertThat(dto.getRequestor().getName()).isNull();

        assertThat(dto.getItems()).isEmpty();
    }

    @Test
    void toDto_mapsEntityToDto() {
        User user = new User();
        user.setId(42L);

        ItemRequest request = new ItemRequest();
        request.setId(7L);
        request.setDescription("Запрос");
        request.setCreated(LocalDateTime.of(2025, 7, 23, 15, 0));
        request.setRequestor(user);

        ItemRequestDto dto = ItemRequestMapper.toDto(request);

        assertThat(dto.getId()).isEqualTo(7L);
        assertThat(dto.getDescription()).isEqualTo("Запрос");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 15, 0));
        assertThat(dto.getRequestorId()).isEqualTo(42L);
    }

    @Test
    void toDto_returnsNullIfInputNull() {
        assertThat(ItemRequestMapper.toDto(null)).isNull();
    }

    @Test
    void toItemRequest_returnsNullIfInputNull() {
        assertThat(ItemRequestMapper.toItemRequest(null)).isNull();
    }

    @Test
    void toResponseDto_returnsNullIfInputNull() {
        assertThat(ItemRequestMapper.toResponseDto(null)).isNull();
    }

    @Test
    void toResponseDto_populatesItemsWhenPresent() {
        User owner = new User();
        owner.setId(50L);

        ru.practicum.shareit.item.model.Item item = new ru.practicum.shareit.item.model.Item();
        item.setId(100L);
        item.setName("Ноутбук");
        item.setOwner(owner);

        ItemRequest request = new ItemRequest();
        request.setId(8L);
        request.setDescription("Хочу ноутбук");
        request.setCreated(LocalDateTime.of(2025, 7, 23, 17, 0));
        request.setItems(List.of(item));

        ItemRequestResponseDto dto = ItemRequestMapper.toResponseDto(request);

        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getItems().get(0).getId()).isEqualTo(100L);
        assertThat(dto.getItems().get(0).getName()).isEqualTo("Ноутбук");
        assertThat(dto.getItems().get(0).getOwnerId()).isEqualTo(50L);
    }
}
