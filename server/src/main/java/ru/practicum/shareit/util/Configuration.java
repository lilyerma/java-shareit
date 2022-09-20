package ru.practicum.shareit.util;

import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

public class Configuration {
    @Bean  // <------------------------------------------------------------- REQUIRED
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
