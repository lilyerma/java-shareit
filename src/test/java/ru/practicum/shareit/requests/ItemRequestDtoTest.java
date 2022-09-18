package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.item.model.ItemView;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig({ItemRequestDto.class})
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L,
                "description description",
                2L,
                LocalDateTime.now());

        ItemView itemView = new ItemView() {
            @Override
            public long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "screw";
            }

            @Override
            public String getDescription() {
                return "standard screw";
            }

            @Override
            public Boolean getAvailable() {
                return true;
            }

            @Override
            public Long getRequestId() {
                return 2L;
            }
        };

        List<ItemView> itemResponse = Arrays.asList(itemView);
        itemRequestDto.setItems(itemResponse);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        String nowTime = now.truncatedTo(ChronoUnit.SECONDS).format(dtf);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description description");
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(nowTime);
        assertThat(result).extractingJsonPathValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.items[0].description").isEqualTo("standard screw");
    }
}
