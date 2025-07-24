package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

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
        LocalDateTime now = LocalDateTime.of(2025, 7, 23, 15, 0, 0);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Отличная вещь");
        commentDto.setAuthorName("Стас");
        commentDto.setCreated(now);

        String jsonContent = json.write(commentDto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"text\":\"Отличная вещь\"");
        assertThat(jsonContent).contains("\"authorName\":\"Стас\"");
        assertThat(jsonContent).contains("\"created\":\"2025-07-23T15:00:00\"");
    }
}
