package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoGw;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long requestorId, BookingDtoGw bookingDtoGw) {
        return post("", requestorId, bookingDtoGw);
    }

    public ResponseEntity<Object> updateStatus(long bookingId, Boolean approved, Long ownerId) {
        String path = "/" + bookingId + "?approved=" + approved;
        return patch(path, ownerId, null, null);
    }

    public ResponseEntity<Object> checkInputStateAndGetForOwnerByState(Long ownerId, String stateStr, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", stateStr,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> getById(long bookingId, Long user) {
        return get("/"+bookingId, user);
    }

    public ResponseEntity<Object> checkInputStateAndGetForBookingUserByState(Long user, String stateStr, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", stateStr,
                "from", from,
                "size", size
        );
        return get("" + "?state={state}&from={from}&size={size}", user, parameters);
    }
}