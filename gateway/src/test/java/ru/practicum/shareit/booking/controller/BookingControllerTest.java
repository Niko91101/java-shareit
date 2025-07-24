package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.validator.BookingValidation;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @MockBean
    private BookingValidation bookingValidation;

    @Test
    void testGetBookings() throws Exception {
        Mockito.when(bookingClient.getBookings(eq(1L), Mockito.any(), eq(0), eq(10)))
                .thenReturn(ResponseEntity.ok(List.of(
                        Map.of("id", 1, "status", "APPROVED"),
                        Map.of("id", 2, "status", "WAITING")
                )));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is("APPROVED")));
    }

    @Test
    void testCreateBooking() throws Exception {
        Mockito.when(bookingClient.bookItem(eq(2L), Mockito.any(BookItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok(Map.of("id", 1, "status", "WAITING")));

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"itemId\":5,\"start\":\"2025-08-01T12:00:00\",\"end\":\"2025-08-02T12:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void testGetBooking() throws Exception {
        Mockito.when(bookingClient.getBooking(eq(1L), eq(1L)))
                .thenReturn(ResponseEntity.ok(Map.of("id", 1, "status", "APPROVED")));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void testApproveBooking() throws Exception {
        Mockito.when(bookingClient.approveBooking(eq(1L), eq(1L), eq(true)))
                .thenReturn(ResponseEntity.ok(Map.of("id", 1, "status", "APPROVED")));

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }
}
