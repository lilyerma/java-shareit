package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.TestPropertySource;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = { "db.name=test"})
public class UserServiceTest {

    @Mock
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    @InjectMocks
    UserService userService = new UserServiceImpl(userRepository);

    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {

        userDto = new UserDto("somename", "some@email.com");
        user = new User(1L, "somename", "some@email.com");
    }


    @Test
    public void when_save_user_without_email_should_throw_exception() {
        UserDto userDto = new UserDto();
        userDto.setName("some name");
        Assert.assertThrows(ValidationException.class, () ->  userService.create(userDto));
    }

    @Test
    public void when_save_user_should_return_user_with_id_from_repo() {

        when(userRepository.save(any())).thenReturn(user);
        Assert.assertEquals(1,userService.create(userDto).getId());
    }

    @Test
    public void when_update_user_should_not_change_email() {
        UserDto userDto = new UserDto();
        userDto.setName("newName");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        user.setName("newName");
        when(userRepository.save(any())).thenReturn(user);
        Assert.assertEquals("newName", userService.update(userDto,1L).getName());
        Assert.assertEquals("some@email.com", userService.update(userDto,1L).getEmail());

    }

    @Test
    public void when_update_user_empty_should_not_change_anything() {
        UserDto userDto = new UserDto();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        Assert.assertEquals("somename", userService.update(userDto,1L).getName());
        Assert.assertEquals("some@email.com", userService.update(userDto,1L).getEmail());
    }

    @Test
    public void when_delete_non_exist_throws_not_found() {
        Assert.assertThrows(NotFoundException.class, () ->  userService.delete(1L));
    }

    @Test
    public void when_delete_calls_delete_method() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.doNothing().when(userRepository).delete(any());
        userService.delete(1L);
        verify(userRepository, times(2));
    }

    @Test
    public void when_call_users_returns_list() {
        User user1 = new User(2L,"second user","second@email.com");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user1));
        Assert.assertEquals(2, userService.getUsers().size());
        Assert.assertEquals(1, userService.getUsers().get(0).getId());
        Assert.assertTrue(userService.getUsers().get(1) instanceof UserDto);
        Assert.assertEquals(2, userService.getUsers().get(1).getId());
    }

    @Test
    public void when_call_users_returns_empty_list() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        Assert.assertEquals(0, userService.getUsers().size());
    }

    @Test
    public void when_call_non_exist_userById_returns_Not_Found() {
        Assert.assertThrows(NotFoundException.class, () ->  userService.delete(1L));
    }

    @Test
    public void when_call_userById_returns_userDto() {
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        UserDto userDto1 = userService.getUserById(1L);
        Assert.assertTrue(userDto1 instanceof UserDto);
        Assert.assertEquals(1, userDto1.getId());
        Assert.assertEquals("some@email.com", userDto1.getEmail());
        verify(userRepository, times(1));
    }

}
