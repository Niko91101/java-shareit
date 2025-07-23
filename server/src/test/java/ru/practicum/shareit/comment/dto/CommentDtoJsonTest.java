package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.of(2025, 7, 23, 20, 0, 0);

        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setText("Отлично работает");
        dto.setAuthorName("Стас");
        dto.setCreated(now);

        JsonContent<CommentDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Отлично работает");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Стас");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-07-23T20:00:00");
    }

    @Test
    void testDeserialize() throws Exception {
        String jsonString = """
                {
                  "id": 3,
                  "text": "Комментарий",
                  "authorName": "Анна",
                  "created": "2025-07-23T21:30:00"
                }
                """;

        CommentDto dto = objectMapper.readValue(jsonString, CommentDto.class);

        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getText()).isEqualTo("Комментарий");
        assertThat(dto.getAuthorName()).isEqualTo("Анна");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 21, 30, 0));
    }
}
