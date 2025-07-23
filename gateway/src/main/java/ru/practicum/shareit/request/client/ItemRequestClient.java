package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    // Создать запрос
    public ResponseEntity<Object> createRequest(long userId, ItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    // Получить все запросы пользователя
    public ResponseEntity<Object> getUserRequests(long userId) {
        return get("", userId);
    }

    // Получить все запросы других пользователей с пагинацией
    public ResponseEntity<Object> getAllRequests(long userId, Integer from, Integer size) {
        Map<String, Object> params = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, params);
    }

    // Получить конкретный запрос
    public ResponseEntity<Object> getRequestById(long userId, long requestId) {
        return get("/" + requestId, userId);
    }
}
