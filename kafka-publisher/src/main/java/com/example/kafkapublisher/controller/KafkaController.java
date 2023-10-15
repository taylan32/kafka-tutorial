package com.example.kafkapublisher.controller;

import com.example.kafkapublisher.dto.Order;
import com.example.kafkapublisher.publisher.KafkaPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class KafkaController {
    private final KafkaPublisher kafkaPublisher;

    public KafkaController(KafkaPublisher kafkaPublisher) {
        this.kafkaPublisher = kafkaPublisher;
    }

    String topicName = "order-create";

    @PostMapping
    public void sendEvent(@RequestBody Order order) {
        kafkaPublisher.publish(topicName, order);
    }

}
