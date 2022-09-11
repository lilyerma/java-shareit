package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.user.dto.BookerDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
@SpringJUnitConfig({BookingDtoNames.class})
public class BookingDtoNamesTest {

        @Autowired
        private JacksonTester<BookingDtoNames> json;

        @Test
        void testItemDto() throws Exception {

            BookingDtoNames bookingDtoNames = new BookingDtoNames(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
            BookerDto bookerDto = new BookerDto(5L);
            ItemDtoShort itemDtoShort = new ItemDtoShort(7L, "hummer");
            bookingDtoNames.setId(1L);
            bookingDtoNames.setBooker(bookerDto);
            bookingDtoNames.setItem(itemDtoShort);
            bookingDtoNames.setStatus(Status.APPROVED);

            JsonContent<BookingDtoNames> result = json.write(bookingDtoNames);


            LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
            LocalDateTime beforeYesterday = LocalDateTime.now().minusDays(2);
            DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
            String yesterdayStr = yesterday.truncatedTo(ChronoUnit.SECONDS).format(dtf);
            String beforeYesterdayStr = beforeYesterday.truncatedTo(ChronoUnit.SECONDS).format(dtf);
            assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
            assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(beforeYesterdayStr);
            assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(yesterdayStr);
            assertThat(result).extractingJsonPathValue("$.booker.id").isEqualTo(5);
            assertThat(result).extractingJsonPathValue("$.status").isEqualTo("APPROVED");
            assertThat(result).extractingJsonPathValue("$.item.id").isEqualTo(7);
            assertThat(result).extractingJsonPathValue("$.item.name").isEqualTo("hummer");
        }
}
