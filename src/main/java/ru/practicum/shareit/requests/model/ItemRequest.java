package ru.practicum.shareit.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@ToString
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String description;

    @Column(name = "requestor_id")
    long requestor;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    public ItemRequest() {
    }
    public ItemRequest(String description, long requestor) {
        this.description = description;
        this.requestor = requestor;
        this.created = LocalDateTime.now();
    }
}
