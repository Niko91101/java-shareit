package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplUnitTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl service;

    private User user;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        request = new ItemRequest();
        request.setId(10L);
        request.setDescription("Need laptop");
        request.setRequestor(user);
    }

    @Test
    void createRequest_throwsIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Test");
        assertThrows(ResponseStatusException.class, () -> service.createRequest(1L, dto));
    }

    @Test
    void createRequest_returnsSavedRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(request);
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Test");
        ItemRequestResponseDto result = service.createRequest(1L, dto);
        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    void getOwnRequests_throwsIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> service.getOwnRequests(1L));
    }

    @Test
    void getOwnRequests_returnsList() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(1L))
                .thenReturn(List.of(request));
        Item result = new Item();
        result.setId(100L);
        result.setName("Test");
        result.setOwner(user);
        when(itemRepository.findByRequest_Id(10L)).thenReturn(List.of(result));
        List<ItemRequestResponseDto> list = service.getOwnRequests(1L);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(10L);
    }

    @Test
    void getAllRequests_throwsIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> service.getAllRequests(1L, 0, 10));
    }

    @Test
    void getAllRequests_returnsList() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorIdNot(eq(1L), any()))
                .thenReturn(List.of(request));
        when(itemRepository.findByRequest_Id(10L)).thenReturn(Collections.emptyList());
        List<ItemRequestResponseDto> list = service.getAllRequests(1L, 0, 10);
        assertThat(list).hasSize(1);
    }

    @Test
    void getRequestById_throwsIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> service.getRequestById(1L, 10L));
    }

    @Test
    void getRequestById_throwsIfRequestNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> service.getRequestById(1L, 10L));
    }

    @Test
    void getRequestById_returnsRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequest_Id(10L)).thenReturn(Collections.emptyList());
        ItemRequestResponseDto result = service.getRequestById(1L, 10L);
        assertThat(result.getId()).isEqualTo(10L);
    }
}
