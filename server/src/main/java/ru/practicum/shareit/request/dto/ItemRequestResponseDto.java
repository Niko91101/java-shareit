package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemRequestResponseDto {
    private Long id;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    private RequestorDto requestor;
    private List<ItemForRequestDto> items;

    @Data
    @NoArgsConstructor
    public static class RequestorDto {
        private Long id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    public static class ItemForRequestDto {
        private Long id;
        private String name;
        private Long ownerId;
    }
}
