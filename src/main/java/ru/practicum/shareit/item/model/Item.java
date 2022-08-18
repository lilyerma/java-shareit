package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String description;
    @Column(name = "is_available", nullable = false)
    Boolean available;
    @Column(name = "owner_id",nullable = false)
    long owner;

    @Column(name = "request_id")
    long request;

    public Item() {
    }

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
