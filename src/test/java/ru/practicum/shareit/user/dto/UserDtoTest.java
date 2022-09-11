package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserDtoTest {


        @Test
        public void givenValidDto_whenValidated_thenNoValidationError() {
            UserDto userDto = getValidUserDto();

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            final Validator validator = factory.getValidator();

            Set<ConstraintViolation<UserDto>> constraintViolations =
                    validator.validate(userDto);

            assertThat(constraintViolations.size()).isZero();

        }

        static Stream<Arguments> provideFieldAndInvalidValue() {
            return Stream.of(
                    Arguments.of("name", null),
                    Arguments.of("email", null)
            );
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("provideFieldAndInvalidValue")
        void testInvalidDto(String fieldName, Object invalidValue) {
            UserDto userDto = getValidUserDto();

            Field field = UserDto.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(userDto, invalidValue);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            final Validator validator = factory.getValidator();

            Set<ConstraintViolation<UserDto>> constraintViolations =
                    validator.validate(userDto);

            assertThat(constraintViolations.size()).isOne();
        }

        private UserDto getValidUserDto() {
            return new UserDto("someName", "something@gmail.com");
        }
    }

