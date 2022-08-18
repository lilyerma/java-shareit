package ru.practicum.shareit.item.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "text", nullable = false)
    String text;
    @Column(name = "item_id", length = 350, nullable = false)
    private long itemId;
    @Column(name = "author_id", nullable = false)
    private long authorId;
    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    public Comment(String text, long authorId, long itemId) {
        this.text = text;
        this.authorId = authorId;
        this.itemId = itemId;
        this.created = LocalDateTime.now();
    }
}
