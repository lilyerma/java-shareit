package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemView;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query(value = " select i from Item i " +
          "where i.requestId = ?1")
    List<ItemView> findProjectionsByRequestId(long requestId);
    List<Item> findItemsByOwner(long owner, Pageable pageable);
    @Query(nativeQuery = true, value = " select * from ITEMS i " +
            "where upper(i.name) like upper(concat('%', ?, '%')) " +
            " or upper(i.description) like upper(concat('%', ?, '%')) and i.IS_AVAILABLE=true")
    List<Item> search(String text, String text2, Pageable pageable);

    void deleteAllByOwner(long id);

    Item getItemById(long id);

    List<Item> getItemByOwner(long id);


}
