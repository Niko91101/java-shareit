package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@test.com");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@test.com");
        user2 = userRepository.save(user2);
    }

    @Test
    void testCreateRequest() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Нужен ноутбук");

        ItemRequestResponseDto response = service.createRequest(user1.getId(), dto);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getDescription()).isEqualTo("Нужен ноутбук");
        assertThat(response.getCreated()).isNotNull();
        assertThat(response.getItems()).isEmpty();

        ItemRequest saved = itemRequestRepository.findById(response.getId()).orElseThrow();
        assertThat(saved.getRequestor().getId()).isEqualTo(user1.getId());

    }

    @Test
    void testGetOwnRequests() {
        createRequestFor(user1, "Запрос 1", LocalDateTime.now().minusDays(1));
        createRequestFor(user1, "Запрос 2", LocalDateTime.now());

        List<ItemRequestResponseDto> result = service.getOwnRequests(user1.getId());

        assertThat(result).hasSize(2);

        assertThat(result.get(0).getDescription()).isEqualTo("Запрос 2");
    }

    @Test
    void testGetAllRequests() {
        createRequestFor(user2, "Чужой запрос 1", LocalDateTime.now().minusHours(2));
        createRequestFor(user2, "Чужой запрос 2", LocalDateTime.now());

        List<ItemRequestResponseDto> result = service.getAllRequests(user1.getId(), 0, 10);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDescription()).isEqualTo("Чужой запрос 2");
    }

    @Test
    void testGetRequestById_withItems() {
        ItemRequest request = createRequestFor(user1, "Запрос с вещами", LocalDateTime.now());

        Item item = new Item();
        item.setName("Ноутбук");
        item.setDescription("MacBook");
        item.setAvailable(true);
        item.setOwner(user2);
        item.setRequest(request);
        itemRepository.save(item);

        ItemRequestResponseDto result = service.getRequestById(user2.getId(), request.getId());

        assertThat(result.getDescription()).isEqualTo("Запрос с вещами");
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("Ноутбук");
        assertThat(result.getItems().get(0).getOwnerId()).isEqualTo(user2.getId());
    }

    @Test
    void testGetRequestById_notFound() {
        assertThrows(ResponseStatusException.class, () ->
                service.getRequestById(user1.getId(), 999L));
    }

    private ItemRequest createRequestFor(User user, String description, LocalDateTime created) {
        ItemRequest request = new ItemRequest();
        request.setDescription(description);
        request.setCreated(created);
        request.setRequestor(user);
        return itemRequestRepository.save(request);
    }
}

