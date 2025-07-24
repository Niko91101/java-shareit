package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    @Test
    void toItem_convertsDtoToEntity() {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Test Item");
        dto.setDescription("Description");
        dto.setAvailable(true);
        dto.setOwnerId(5L);

        Item item = ItemMapper.toItem(dto);
        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("Test Item");
        assertThat(item.getDescription()).isEqualTo("Description");
        assertThat(item.getAvailable()).isTrue();
    }

    @Test
    void toDto_convertsEntityToDto() {
        Item item = new Item();
        User owner = new User();
        owner.setId(3L);
        item.setId(2L);
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(false);
        item.setOwner(owner);

        ItemDto dto = ItemMapper.toDto(item);
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("Item");
        assertThat(dto.getDescription()).isEqualTo("Desc");
        assertThat(dto.getAvailable()).isFalse();
        assertThat(dto.getOwnerId()).isEqualTo(3L);
    }

    @Test
    void toDto_returnsNullIfItemIsNull() {
        ItemDto dto = ItemMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void toDto_handlesNullOwnerAndRequest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto dto = ItemMapper.toDto(item);

        assertThat(dto.getOwnerId()).isNull();
        assertThat(dto.getRequestId()).isNull();
    }

    @Test
    void toItem_returnsNullIfDtoIsNull() {
        Item item = ItemMapper.toItem(null);
        assertThat(item).isNull();
    }


}
