package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void testCreateBooking() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusDays(1));

        BookingResponseDto response = new BookingResponseDto();
        response.setId(42L);
        response.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingService.create(eq(1L), any(BookingDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(42)))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void testApproveBooking() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(5L);
        response.setStatus(BookingStatus.APPROVED);

        Mockito.when(bookingService.approve(1L, 5L, true)).thenReturn(response);

        mockMvc.perform(patch("/bookings/5")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void testGetBooking() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(7L);
        response.setStatus(BookingStatus.REJECTED);

        Mockito.when(bookingService.get(2L, 7L)).thenReturn(response);

        mockMvc.perform(get("/bookings/7")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.status", is("REJECTED")));
    }

    @Test
    void testGetAllByUser() throws Exception {
        BookingResponseDto b1 = new BookingResponseDto();
        b1.setId(1L);
        BookingResponseDto b2 = new BookingResponseDto();
        b2.setId(2L);

        Mockito.when(bookingService.getAllByUser(eq(1L), any()))
                .thenReturn(List.of(b1, b2));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void testGetAllByOwner() throws Exception {
        BookingResponseDto b = new BookingResponseDto();
        b.setId(100L);

        Mockito.when(bookingService.getAllByOwner(eq(5L), any()))
                .thenReturn(List.of(b));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(100)));
    }
}
