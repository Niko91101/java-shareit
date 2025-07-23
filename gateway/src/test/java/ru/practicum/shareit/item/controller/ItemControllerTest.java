package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.validator.TextValidator;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Import(TextValidator.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TextValidator textValidator;

    @MockBean
    private ItemClient itemClient;

    @Test
    void testGetItemsByOwner() throws Exception {
        Mockito.when(itemClient.getUserItems(eq(1L), eq(0), eq(10)))
                .thenReturn(ResponseEntity.ok(List.of(
                        Map.of("id", 1, "name", "Дрель"),
                        Map.of("id", 2, "name", "Отвёртка")
                )));

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Дрель")))
                .andExpect(jsonPath("$[1].name", is("Отвёртка")));
    }

    @Test
    void testSearchItems() throws Exception {
        Mockito.when(itemClient.searchItems(eq(1L), eq("дрель"), eq(0), eq(10)))
                .thenReturn(ResponseEntity.ok(List.of(
                        Map.of("id", 1, "name", "Дрель")
                )));

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "дрель")
                        .param("from", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Дрель")));
    }

    @Test
    void testGetItem() throws Exception {
        Mockito.when(itemClient.getItem(eq(2L), eq(1L)))
                .thenReturn(ResponseEntity.ok(Map.of(
                        "id", 1,
                        "name", "Дрель",
                        "description", "Мощная дрель"
                )));

        mockMvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Дрель")))
                .andExpect(jsonPath("$.description", is("Мощная дрель")));
    }

    @Test
    void testCreateItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Перфоратор");
        itemDto.setDescription("Мощный инструмент");
        itemDto.setAvailable(true);

        Mockito.when(itemClient.createItem(eq(1L), Mockito.any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok(Map.of(
                        "id", 1,
                        "name", "Перфоратор"
                )));

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"name\":\"Перфоратор\",\"description\":\"Мощный инструмент\",\"available\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Перфоратор")));
    }

    @Test
    void testUpdateItem() throws Exception {
        Mockito.when(itemClient.updateItem(eq(1L), eq(2L), Mockito.any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok(Map.of(
                        "id", 2,
                        "name", "Молоток"
                )));

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/2")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"name\":\"Молоток\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Молоток")));
    }

    @Test
    void testAddComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Отличная вещь");
        commentDto.setCreated(LocalDateTime.now());

        Mockito.when(itemClient.addComment(eq(2L), eq(1L), Mockito.any(CommentDto.class)))
                .thenReturn(ResponseEntity.ok(Map.of(
                        "id", 1,
                        "text", "Отличная вещь"
                )));

        mockMvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"text\":\"Отличная вещь\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("Отличная вещь")));
    }
}
