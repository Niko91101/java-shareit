package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestResponseDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestResponseDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.of(2025, 7, 23, 15, 30, 0);

        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(1L);
        dto.setDescription("Нужен ноутбук");
        dto.setCreated(now);

        ItemRequestResponseDto.RequestorDto requestor = new ItemRequestResponseDto.RequestorDto();
        requestor.setId(10L);
        requestor.setName("Стас");
        dto.setRequestor(requestor);

        ItemRequestResponseDto.ItemForRequestDto item = new ItemRequestResponseDto.ItemForRequestDto();
        item.setId(100L);
        item.setName("MacBook");
        item.setOwnerId(20L);
        dto.setItems(List.of(item));

        JsonContent<ItemRequestResponseDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужен ноутбук");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-07-23T15:30:00");
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id").isEqualTo(10);
        assertThat(result).extractingJsonPathStringValue("$.requestor.name").isEqualTo("Стас");
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
    }

    @Test
    void testDeserialize() throws Exception {
        String jsonString = """
                {
                  "id": 5,
                  "description": "Ищу фотоаппарат",
                  "created": "2025-07-23T12:00:00",
                  "requestor": {
                    "id": 2,
                    "name": "Анна"
                  },
                  "items": []
                }
                """;

        ItemRequestResponseDto dto = objectMapper.readValue(jsonString, ItemRequestResponseDto.class);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getDescription()).isEqualTo("Ищу фотоаппарат");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 12, 0, 0));
        assertThat(dto.getRequestor().getName()).isEqualTo("Анна");
        assertThat(dto.getItems()).isEmpty();
    }
}
