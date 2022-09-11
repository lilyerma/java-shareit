package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig({ItemDto.class})
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;
    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto("screw", "standard screw", true);
        itemDto.setId(1L);
        ItemDto itemDto1 = new ItemDto("hummer", "heavy hummer", false);
        itemDto1.setId(2L);
        BookingDto lastBooking = new BookingDto(1L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), 2L);
        BookingDto nextBooking = new BookingDto(2L,LocalDateTime.now().plusDays(1),

                LocalDateTime.now().plusDays(2), 3L);
        List<CommentDto> comments = Arrays.asList(new CommentDto(1L, "good item",
                LocalDateTime.now().minusDays(2)), new CommentDto(2L, "very good",
                LocalDateTime.now().minusDays(1)));

        itemDto.setNextBooking(nextBooking);
        itemDto.setLastBooking(lastBooking);
        itemDto.setComments(comments);

        JsonContent<ItemDto> result = json.write(itemDto);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        String nowTime = now.truncatedTo(ChronoUnit.SECONDS).format(dtf);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("screw");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("standard screw");
        assertThat(result).extractingJsonPathValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.nextBooking.id").isEqualTo(2);
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(2);

    }

}
