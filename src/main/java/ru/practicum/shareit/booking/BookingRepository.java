package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerOrderByIdDesc(long booker);
    List<Booking> findByBookerAndEndBeforeOrderByIdDesc(long booker, LocalDateTime now);
    List<Booking> findByBookerAndStartAfterOrderByIdDesc(long booker, LocalDateTime now);
    List<Booking> findByBookerAndStartBeforeAndEndAfterOrderByIdDesc(long booker, LocalDateTime now, LocalDateTime now1);

    List<Booking> findByBookerAndStatusOrderByIdDesc(long booker, Status status);

    @Query(" select b from Booking b " +
            "left join Item i on i.id=b.itemId where i.owner = ?1 and b.end < ?2 order by b.id desc")
    List<Booking> findBookingByOwnerAndStatePast(long owner, LocalDateTime now);

    @Query(" select b from Booking b " +
            "left join Item i on i.id=b.itemId where i.owner=?1 and b.start < ?2 and b.end > ?2 order by b.id desc")
    List<Booking> findBookingByOwnerAndStateCurrent(long owner, LocalDateTime now);

    @Query(nativeQuery=true, value = "select * from bookings b " +
            "where b.booker_id =? and b.item_id=? and b.start_date < ? and b.status = 'APPROVED' limit 1")
    Optional<Booking> findOneByBookingByBookerAndItemIdAndStartBeforeAndStatus(long booker, long item,
                                                                      LocalDateTime now);


    @Query(nativeQuery=true, value = " select * from bookings b " +
            "where b.item_id =? and b.status<>'REJECTED' AND b.start_date > ? ORDER BY b.start_date asc limit 2")
    List<Booking> findBookingByItemIdTwoNext(long itemId, LocalDateTime now);

    @Query(nativeQuery=true, value = " select * from bookings b " +
            "where b.item_id =? and b.status<>'REJECTED' AND b.start_date < ? ORDER BY b.end_date desc limit 2")
    List<Booking> findBookingByItemIdTwoLast(long itemId, LocalDateTime now);


    @Query(" select b from Booking b " +
            "left join Item i on i.id=b.itemId where i.owner=?1 and b.start > ?2 order by b.id desc")
    List<Booking> findBookingByOwnerAndStateFuture(long owner, LocalDateTime now);

    @Query(" select b from Booking b " +
            "left join Item i on i.id=b.itemId where i.owner=?1 and b.status=?2 order by b.id desc")
    List<Booking> findBookingByOwnerAndState(long owner, Status status);

    @Query(" select b from Booking b " +
            "left join Item i on i.id=b.itemId where i.owner=?1 and b.status<>'REJECTED' order by b.id desc")
    List<Booking> findBookingByOwnerAndNotRejected(long owner);

}
