package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(nativeQuery=true, value = " select * from BOOKINGS b " +
            "where b.BOOKER_ID=? order by b.id desc")
    List<Booking> findByBookerOrderByIdDescPagable(long booker, Pageable pageable);

    List<Booking> findByBookerOrderByIdDesc(long booker);
    @Query(nativeQuery=true, value = " select * from BOOKINGS b " +
            "where b.BOOKER_ID=? and b.END_DATE <? order by b.id desc")
    List<Booking> findByBookerAndEndBeforeOrderByIdDesc(long booker, LocalDateTime now, Pageable pageable);
    @Query(nativeQuery=true, value = " select * from BOOKINGS b " +
            "where b.BOOKER_ID=? and b.START_DATE > ? order by b.id desc")
    List<Booking> findByBookerAndStartAfterOrderByIdDesc(long booker, LocalDateTime now, Pageable pageable);
    @Query(nativeQuery=true, value = " select * from BOOKINGS b " +
            "where b.BOOKER_ID=? and b.START_DATE < ? and END_DATE > ? order by b.id desc")
    List<Booking> findByBookerAndStartBeforeAndEndAfterOrderByIdDesc(long booker, LocalDateTime now, LocalDateTime now1,
                                                                     Pageable pageable);

    List<Booking> findByBookerAndStatus(long booker, Status status, Pageable pageable);

    @Query(nativeQuery=true, value = " select * from BOOKINGS b " +
            "left join ITEMS i on i.id=b.ITEM_ID where i.OWNER_ID = ? and b.END_DATE < ? order by b.id desc")
    List<Booking> findBookingByOwnerAndStatePast(long owner, LocalDateTime now, Pageable pageable);

    @Query(value = " select b from Booking b " +
            "left join Item i on i.id=b.itemId where i.owner=?1 and b.start < ?2 and b.end > ?2 " +
            "order by b.id desc")
    List<Booking> findBookingByOwnerAndStateCurrent(long owner, LocalDateTime now, Pageable pageable);

    @Query(nativeQuery=true, value = "select * from bookings b " +
            "where b.booker_id =? and b.item_id=? and b.start_date < ? and b.status = 'APPROVED' limit 1")
    Optional<Booking> findOneByBookingByBookerAndItemIdAndStartBeforeAndStatus(long booker, long item,
                                                                      LocalDateTime now);


    @Query(nativeQuery=true, value = " select * from bookings b " +
            "where b.item_id =? and b.status<>'REJECTED' AND b.start_date > ? limit 2")
    List<Booking> findBookingByItemIdTwoNext(long itemId, LocalDateTime now);

    @Query(nativeQuery=true, value = " select * from bookings b " +
            "where b.item_id =? and b.status<>'REJECTED' AND b.start_date < ? ORDER BY b.end_date desc limit 2")
    List<Booking> findBookingByItemIdTwoLast(long itemId, LocalDateTime now);


    @Query(nativeQuery=true, value = " select * from bookings b " +
            "left join ITEMS i on i.id=b.ITEM_ID where i.OWNER_ID=? and b.START_DATE > ? order by b.id desc")
    List<Booking> findBookingByOwnerAndStateFuture(long owner, LocalDateTime now, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "left join Item i on i.id=b.itemId where i.owner=?1 and b.status=?2")
    List<Booking> findBookingByOwnerAndState(long owner, Status status, Pageable pageable);

    @Query( value = " select b from Booking b " +
            "left join Item i on i.id=b.itemId where i.owner=?1")
    List<Booking> findBookingByOwnerAndNotRejected(long owner, Pageable pageable);

}
