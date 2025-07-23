package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(10L);
        itemDto.setName("Дрель");
        itemDto.setDescription("Аккумуляторная");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(5L);
        itemDto.setRequestId(3L);

        String jsonContent = json.write(itemDto).getJson();

        assertThat(jsonContent).contains("\"id\":10");
        assertThat(jsonContent).contains("\"name\":\"Дрель\"");
        assertThat(jsonContent).contains("\"description\":\"Аккумуляторная\"");
        assertThat(jsonContent).contains("\"available\":true");
        assertThat(jsonContent).contains("\"ownerId\":5");
        assertThat(jsonContent).contains("\"requestId\":3");
    }

    @Test
    void testDeserialize() throws Exception {
        String jsonContent = """
                {
                  "id": 11,
                  "name": "Отвертка",
                  "description": "Новая",
                  "available": false,
                  "ownerId": 7,
                  "requestId": null
                }
                """;

        ItemDto result = objectMapper.readValue(jsonContent, ItemDto.class);

        assertThat(result.getId()).isEqualTo(11L);
        assertThat(result.getName()).isEqualTo("Отвертка");
        assertThat(result.getDescription()).isEqualTo("Новая");
        assertThat(result.getAvailable()).isFalse();
        assertThat(result.getOwnerId()).isEqualTo(7L);
        assertThat(result.getRequestId()).isNull();
    }
}
