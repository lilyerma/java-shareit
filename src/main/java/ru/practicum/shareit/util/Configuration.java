package ru.practicum.shareit.util;

import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import ru.practicum.shareit.requests.ItemRequestController;

public class Configuration {
    @Bean  // <------------------------------------------------------------- REQUIRED
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
