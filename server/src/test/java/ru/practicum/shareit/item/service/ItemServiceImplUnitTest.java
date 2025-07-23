package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validation.ItemValidator;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ItemServiceImplUnitTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemValidator itemValidator;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestRepository requestRepository;

    @InjectMocks
    private ItemServiceImpl service;

    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        owner.setId(1L);
        owner.setEmail("owner@test.com");

        item = new Item();
        item.setId(10L);
        item.setName("Test Item");
        item.setOwner(owner);
    }

    @Test
    void getItem_throwsIfItemNotFound() {
        when(itemRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.getItem(10L, 1L));
    }

    @Test
    void addItem_throwsIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ItemDto dto = new ItemDto();
        dto.setName("Item");

        assertThrows(ResponseStatusException.class,
                () -> service.addItem(1L, dto));
    }

    @Test
    void addComment_throwsIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CommentDto dto = new CommentDto();
        dto.setText("Text");

        assertThrows(ResponseStatusException.class,
                () -> service.addComment(1L, 10L, dto));
    }

    @Test
    void search_returnsEmptyIfBlank() {
        assertThat(service.search("   ")).isEmpty();
    }

    @Test
    void getItemsByOwner_returnsEmptyIfNoItems() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findByOwnerId(1L)).thenReturn(Collections.emptyList());

        assertThat(service.getItemsByOwner(1L)).isEmpty();
    }
}
