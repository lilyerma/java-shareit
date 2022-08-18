package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    LocalDateTime end;
    @Column(name = "item_id", nullable = false)
    long itemId;
    @Column(name = "booker_id", nullable = false)
    long booker;
    @Column
    @Enumerated(EnumType.STRING)
    Status status;


    public Booking(LocalDateTime start, LocalDateTime end, Long itemId) {
        this.start=start;
        this.end=end;
        this.itemId = itemId;
    }
}
