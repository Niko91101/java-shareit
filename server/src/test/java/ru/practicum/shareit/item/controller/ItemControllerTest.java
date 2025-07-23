package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void testGetItem() throws Exception {
        CommentDto comment = new CommentDto(1L, "Отличная вещь", "Петя", LocalDateTime.now());
        ItemResponseDto responseDto = new ItemResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Дрель");
        responseDto.setDescription("Описание");
        responseDto.setAvailable(true);
        responseDto.setComments(List.of(comment));

        when(itemService.getItem(anyLong(), anyLong())).thenReturn(responseDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Дрель")))
                .andExpect(jsonPath("$.comments[0].text", is("Отличная вещь")));
    }

    @Test
    void testAddItem() throws Exception {
        ItemDto request = new ItemDto();
        request.setName("Молоток");
        request.setDescription("Новый");
        request.setAvailable(true);

        ItemDto response = new ItemDto();
        response.setId(1L);
        response.setName("Молоток");
        response.setDescription("Новый");
        response.setAvailable(true);

        when(itemService.addItem(anyLong(), any(ItemDto.class))).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Молоток")))
                .andExpect(jsonPath("$.description", is("Новый")));
    }

    @Test
    void testGetItemsByOwner() throws Exception {
        ItemResponseDto item1 = new ItemResponseDto();
        item1.setId(1L);
        item1.setName("Дрель");

        ItemResponseDto item2 = new ItemResponseDto();
        item2.setId(2L);
        item2.setName("Отвёртка");

        when(itemService.getItemsByOwner(anyLong())).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Дрель")))
                .andExpect(jsonPath("$[1].name", is("Отвёртка")));
    }

    @Test
    void testSearchItems() throws Exception {
        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Описание");
        item.setAvailable(true);

        when(itemService.search("дрель")).thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "дрель")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Дрель")));
    }

    @Test
    void testAddComment() throws Exception {
        CommentDto comment = new CommentDto(1L, "Хорошая вещь", "Иван", LocalDateTime.now());

        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(comment);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", is("Хорошая вещь")))
                .andExpect(jsonPath("$.authorName", is("Иван")));
    }
}
