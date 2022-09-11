package ru.practicum.shareit.item.model;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@JsonTest
@SpringJUnitConfig({ItemDto.class})
public class CommentMapperTest {

    @InjectMocks
    CommentMapper commentMapper;

    Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment("Some text", 1L, 2L);
        comment.setId(3L);
    }

    @Test
    void toCommentDto() {

        Assert.assertEquals(3L, commentMapper.toCommentDto(comment).getId());
        Assert.assertEquals("Some text", commentMapper.toCommentDto(comment).getText());
        Assert.assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                commentMapper.toCommentDto(comment).getCreated().truncatedTo(ChronoUnit.SECONDS));
    }
    
}
