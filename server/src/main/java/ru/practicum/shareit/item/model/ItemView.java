package ru.practicum.shareit.item.model;

public interface ItemView {

    long getId();
    String getName();
    String getDescription();
    Boolean getAvailable();
    Long getRequestId();
}
