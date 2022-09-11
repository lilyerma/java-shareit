package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("нельзя чтобы был пустой email");
        }
        User user = userMapper.fromUserDto(userDto);
        User userToReturn = userRepository.save(user);
        return UserMapper.toUserDto(userToReturn);
    }

    @Override
    public void checkId(Long id) {
        if (userRepository.findById(id).isEmpty()){
            throw new NotFoundException("нет пользователя");
        }
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователь с таким Id не найден");
        }
        User existUser = userRepository.getReferenceById(id);
        if (userDto.getEmail()!=null){
            existUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName()!=null){
            existUser.setName(userDto.getName());
        }
        User userToReturn = userRepository.save(existUser);
        return UserMapper.toUserDto(userToReturn);
    }
    @Transactional
    @Override
    public void delete(long id) {
        User user = userRepository.getReferenceById(id);
        userRepository.delete(user);

    }

    @Override
    public List<UserDto> getUsers() {
        return   userRepository.findAll()
                .stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long id) {
        try {
            User userToReturn = userRepository.getReferenceById(id);
            return UserMapper.toUserDto(userToReturn);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Нет пользователя с таким номером");
        }
    }

}
