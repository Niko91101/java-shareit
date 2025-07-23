package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Стас");
        userDto.setEmail("stas@example.com");

        String jsonContent = json.write(userDto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"name\":\"Стас\"");
        assertThat(jsonContent).contains("\"email\":\"stas@example.com\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String jsonContent = """
                {
                  "id": 2,
                  "name": "Анна",
                  "email": "anna@example.com"
                }
                """;

        UserDto result = objectMapper.readValue(jsonContent, UserDto.class);

        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Анна");
        assertThat(result.getEmail()).isEqualTo("anna@example.com");
    }
}
