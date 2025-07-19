package ru.practicum.shareit.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;

@UtilityClass
public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }

    public static Comment toComment(CommentDto dto) {
        if (dto == null) return null;

        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setText(dto.getText());
        comment.setCreated(dto.getCreated());

        return comment;
    }
}
