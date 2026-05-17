package com.example.config;

import com.example.service.UserEventPublisher;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestKafkaConfig {

    @Bean
    @Primary
    public UserEventPublisher mockUserEventPublisher() {
        return Mockito.mock(UserEventPublisher.class);
    }
}