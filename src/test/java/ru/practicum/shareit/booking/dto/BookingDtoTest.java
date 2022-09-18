package ru.practicum.shareit.booking.dto;

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


import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig({BookingDto.class})
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testItemDto() throws Exception {

        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), 2L);

        JsonContent<BookingDto> result = json.write(bookingDto);
        ObjectMapper objectMapper = new ObjectMapper();

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime beforeYesterday = LocalDateTime.now().minusDays(2);
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        String yesterdayStr = yesterday.truncatedTo(ChronoUnit.SECONDS).format(dtf);
        String beforeYesterdayStr = beforeYesterday.truncatedTo(ChronoUnit.SECONDS).format(dtf);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(beforeYesterdayStr);
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(yesterdayStr);
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathValue("$.status").isEqualTo(null);
        assertThat(result).extractingJsonPathValue("$.itemId").isEqualTo(null);
    }


}
