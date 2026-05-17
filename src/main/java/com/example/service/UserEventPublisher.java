package com.example.service;


import com.example.dto.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public void publishUserEvent(String operation, String email) {
        UserEvent event = new UserEvent(operation, email);
        log.info("Publishing user event to Kafka: {}", event);
        kafkaTemplate.send(TOPIC, email, event);
    }
}