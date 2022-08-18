package ru.practicum.shareit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // помечает класс как java-config для контекста приложения
@EnableWebMvc  // призывает импортировать дополнительную конфигурацию для веб-приложений
public class WebConfig {

}