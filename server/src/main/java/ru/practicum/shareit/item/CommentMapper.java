package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getCreated()
        );

        return commentDto;
    }

}
