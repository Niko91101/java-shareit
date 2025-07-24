package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ItemServiceImplTest {

    @Autowired
    private ItemService service;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setup() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@test.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Гитара");
        item.setDescription("Акустическая");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);
    }

    @Test
    void testAddItem() {
        ItemDto dto = new ItemDto();
        dto.setName("Барабаны");
        dto.setDescription("Ударная установка");
        dto.setAvailable(true);

        ItemDto result = service.addItem(owner.getId(), dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Барабаны");
    }

    @Test
    void testUpdateItem_asOwner() {
        ItemDto dto = new ItemDto();
        dto.setName("Гитара новая");
        dto.setAvailable(false);

        ItemDto result = service.updateItem(owner.getId(), item.getId(), dto);

        assertThat(result.getName()).isEqualTo("Гитара новая");
        assertThat(result.getAvailable()).isFalse();
    }

    @Test
    void testUpdateItem_asNonOwnerThrows() {
        ItemDto dto = new ItemDto();
        dto.setName("Чужая вещь");

        assertThrows(ResponseStatusException.class, () ->
                service.updateItem(booker.getId(), item.getId(), dto));
    }

    @Test
    void testGetItem_withComments() {
        Comment comment = new Comment();
        comment.setText("Отличная вещь");
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        ItemResponseDto result = service.getItem(item.getId(), booker.getId());

        assertThat(result.getId()).isEqualTo(item.getId());
        assertThat(result.getComments()).hasSize(1);
        assertThat(result.getComments().get(0).getText()).isEqualTo("Отличная вещь");
    }

    @Test
    void testSearchItems() {
        List<ItemDto> result = service.search("гитара");
        assertThat(result).hasSize(1);

        List<ItemDto> empty = service.search("");
        assertThat(empty).isEmpty();
    }

    @Test
    void testAddComment_asBookerAfterBooking() {

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Спасибо!");

        CommentDto result = service.addComment(booker.getId(), item.getId(), commentDto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getText()).isEqualTo("Спасибо!");
    }

    @Test
    void testAddComment_withoutBookingThrows() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Не могу оставить");

        assertThrows(ResponseStatusException.class, () ->
                service.addComment(booker.getId(), item.getId(), commentDto));
    }
}
