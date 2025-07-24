package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RequestControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createRequest_returnsCreated() throws Exception {
        ItemRequestResponseDto response = new ItemRequestResponseDto();
        response.setId(1L);
        response.setDescription("Test");
        response.setCreated(LocalDateTime.now());
        when(requestService.createRequest(eq(1L), any(ItemRequestDto.class)))
                .thenReturn(response);

        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Test");

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getOwnRequests_returnsList() throws Exception {
        ItemRequestResponseDto response = new ItemRequestResponseDto();
        response.setId(2L);
        response.setDescription("Own");
        response.setCreated(LocalDateTime.now());
        when(requestService.getOwnRequests(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void getAllRequests_returnsList() throws Exception {
        ItemRequestResponseDto response = new ItemRequestResponseDto();
        response.setId(3L);
        response.setDescription("Other");
        response.setCreated(LocalDateTime.now());
        when(requestService.getAllRequests(1L, 0, 10))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(3));
    }

    @Test
    void getRequestById_returnsRequest() throws Exception {
        ItemRequestResponseDto response = new ItemRequestResponseDto();
        response.setId(4L);
        response.setDescription("ById");
        response.setCreated(LocalDateTime.now());
        when(requestService.getRequestById(1L, 4L))
                .thenReturn(response);

        mockMvc.perform(get("/requests/4")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.description").value("ById"));
    }
}
