package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.Status.*;

@ExtendWith(MockitoExtension.class)
@Import(ValidationAutoConfiguration.class)
public class ItemServiceImplTest {


    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    ObjectMapper mapper = new ObjectMapper();

    ItemDto itemDto;
    ItemDto itemDto2;
    Item item;
    Item item2;
    Booking bookingPast;
    Booking bookingPresent;
    Booking bookingFuture;
    Booking bookingRejected;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto("Screw", "good screw", true);
        itemDto = new ItemDto("Hummer", "good hummer", true);
        item = new Item("Screw", "good screw", true);
        item2 = new Item("Hummer", "good hummer", true);
        item.setId(1L);

        bookingPast = new Booking(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1),
                1L);
        bookingPresent = new Booking(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(1),
                1L);
        bookingFuture = new Booking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3),
                1L);
        bookingRejected = new Booking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3),
                1L);

        bookingPast.setStatus(APPROVED);
        bookingPast.setId(1L);
        bookingPresent.setStatus(APPROVED);
        bookingPresent.setId(2L);
        bookingFuture.setStatus(WAITING);
        bookingFuture.setId(3L);
        bookingRejected.setStatus(REJECTED);
        bookingRejected.setId(4L);

    }

    @Test
    void create() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.save(any())).thenReturn(item);
        Assertions.assertEquals(itemService.create(itemDto, 3L).getId(), 1);
    }


    //Проверяет, что объект сущестует и что пользователь его владелец

    @Test
    void checkOwnership() {
        when(itemRepository.findById(anyLong())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> itemService.checkOwnership(1L, 1L));
    }

    @Test
    void update() {
        item.setOwner(1L);
        ItemUpdateDto itemUpd = new ItemUpdateDto("super screw", "updated good screw",
                false, 1L);
        Item item1 = new Item("super screw", "updated good screw",
                false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.getReferenceById(1L)).thenReturn(item);
        when(itemRepository.save(any())).thenReturn(item1);
        Assertions.assertEquals(itemService.update(itemUpd, 1L, 1L).getDescription(), "updated good screw");

    }

    @Test
    public void delete() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.getReferenceById(1L)).thenReturn(item);
        Assertions.assertThrows(NotFoundException.class, () -> itemService.delete(1L, 2L));
    }


    @Test
    void getOwnerByItemId() {
        item.setOwner(3L);
        when(itemRepository.getItemById(1L)).thenReturn(item);
        Assertions.assertEquals(3L, itemService.getOwnerByItemId(1L));
    }

    @Test
    void getUserItems() {
        Item item2 = new Item("Hummer", "good hummer", true);
        item2.setId(2L);
        when(bookingRepository.findBookingByItemIdTwoNext(anyLong(), any()))
                .thenReturn(Arrays.asList(bookingFuture, bookingPast));
        when(itemRepository.findItemsByOwner(anyLong(), any())).thenReturn(Arrays.asList(item, item2));
        Assertions.assertEquals(2, itemService.getUserItems(1L, 1, 10).size());
        Assertions.assertEquals(bookingPast.getId(), itemService.getUserItems(1L, 1, 10).get(0).getId());
    }

    @Test
    void checkAvailable() {
        item.setAvailable(false);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        Assertions.assertThrows(ValidationException.class, () -> itemService.checkAvailable(1L));
    }

    @Test
    void addBookingDates() {
        when(bookingRepository.findBookingByItemIdTwoNext(anyLong(), any()))
                .thenReturn(Arrays.asList(bookingFuture, bookingPast));
        Assertions.assertEquals(bookingPast.getId(), itemService.addBookingDates(itemDto).getNextBooking().getId());
    }


    //Ищем по имени и описанию
    @Test
    void searchNameAndDesc() {
        when(itemRepository.search(anyString(), anyString(), any())).thenReturn(Arrays.asList(item, item2));
        Assertions.assertEquals(2, itemService.searchNameAndDesc("some text", 0, 20).size());
        Assertions.assertTrue(itemService.searchNameAndDesc("some text", 0, 20).get(0) instanceof ItemDto);
    }

    // Метод для получения одного предмета для владельца и для пользователя
    @Test
    void getItemByIdForOwnerOrForUser() {
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        comment1.setText("this is a greate item");
        comment2.setText("very nice srew");
        User user = new User(1L, "John", "email@email.com");
        item.setOwner(3L);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(commentRepository.findCommentByItemId(anyLong()))
                .thenReturn(Arrays.asList(comment1, comment2));
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        Assertions.assertTrue(itemService.getItemByIdForOwnerOrForUser(1L, 1L) instanceof ItemDto);
        Assertions.assertEquals(2, itemService.getItemByIdForOwnerOrForUser(1L, 1L).getComments().size());

    }

    @Test
    void getItemById() {
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(1L));
    }


    //Метод для добавления бронирований к предмету
    @Test
    void getItemByIdWithBookings() {
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        comment1.setText("this is a greate item");
        comment2.setText("very nice srew");
        User user = new User(1L, "John", "email@email.com");
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(commentRepository.findCommentByItemId(anyLong()))
                .thenReturn(Arrays.asList(comment1, comment2));
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingByItemIdTwoNext(anyLong(), any()))
                .thenReturn(Arrays.asList(bookingFuture, bookingPast));
        Assertions.assertEquals(3, itemService.getItemByIdWithBookings(1L).getLastBooking().getId());
        Assertions.assertEquals(1, itemService.getItemByIdWithBookings(1L).getNextBooking().getId());
    }

    //Метод для создания комментария
    @Test
    void createComment() {
        CommentDto commentDto1 = new CommentDto();
        commentDto1.setText("nice, very good");
        commentDto1.setId(1L);
        Comment comment1 = new Comment();
        comment1.setText("nice, very good");
        User user = new User(1L, "John", "email@email.com");
        when(commentRepository.save(any())).thenReturn(comment1);

        when(bookingRepository.findOneByBookingByBookerAndItemIdAndStartBeforeAndStatus(anyLong(), anyLong(),
                any())).thenReturn(Optional.ofNullable(bookingPast));
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        Assertions.assertEquals("John", itemService.createComment(1L, 1L, commentDto1).getAuthorName());
    }

}
