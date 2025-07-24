package ru.practicum.shareit.comment.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    @Test
    void toDto_mapsEntityToDto() {
        User author = new User();
        author.setId(5L);
        author.setName("Author");

        Item item = new Item();
        item.setId(7L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Good item");
        comment.setCreated(LocalDateTime.of(2025, 7, 23, 12, 0));
        comment.setAuthor(author);
        comment.setItem(item);

        CommentDto dto = CommentMapper.toDto(comment);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Good item");
        assertThat(dto.getAuthorName()).isEqualTo("Author");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 12, 0));
    }

    @Test
    void toDto_mapsWithAuthorAndItem() {
        User author = new User();
        author.setId(10L);
        author.setName("Автор");

        Item item = new Item();
        item.setId(20L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Текст комментария");
        comment.setCreated(LocalDateTime.of(2025, 7, 23, 12, 0));
        comment.setAuthor(author);
        comment.setItem(item);

        CommentDto dto = CommentMapper.toDto(comment);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Текст комментария");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 12, 0));
        assertThat(dto.getAuthorName()).isEqualTo("Автор");
    }

    @Test
    void toDto_returnsFullFields() {
        User author = new User();
        author.setId(10L);
        author.setName("Автор");

        Item item = new Item();
        item.setId(20L);

        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("Полный комментарий");
        comment.setCreated(LocalDateTime.of(2025, 7, 23, 16, 30));
        comment.setAuthor(author);
        comment.setItem(item);

        CommentDto dto = CommentMapper.toDto(comment);

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getText()).isEqualTo("Полный комментарий");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 16, 30));
        assertThat(dto.getAuthorName()).isEqualTo("Автор");
    }

    @Test
    void toDto_returnsNullIfCommentIsNull() {
        CommentDto dto = CommentMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void toComment_mapsDtoToEntity() {
        CommentDto dto = new CommentDto();
        dto.setId(10L);
        dto.setText("Text");
        dto.setCreated(LocalDateTime.of(2025, 7, 23, 18, 0));

        Comment comment = CommentMapper.toComment(dto);

        assertThat(comment.getId()).isEqualTo(10L);
        assertThat(comment.getText()).isEqualTo("Text");
        assertThat(comment.getCreated()).isEqualTo(LocalDateTime.of(2025, 7, 23, 18, 0));
    }

    @Test
    void toComment_returnsNullIfDtoIsNull() {
        Comment comment = CommentMapper.toComment(null);
        assertThat(comment).isNull();
    }
}
