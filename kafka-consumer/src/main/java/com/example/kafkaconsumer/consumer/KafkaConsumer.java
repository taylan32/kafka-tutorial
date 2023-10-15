package com.example.kafkaconsumer.consumer;

import com.example.kafkaconsumer.consumer.exception.BusinessException;
import com.example.kafkaconsumer.consumer.exception.ValidationException;
import com.example.kafkaconsumer.model.event.OrderEvent;
import com.example.kafkaconsumer.publisher.KafkaPublisher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class KafkaConsumer {

    private final KafkaPublisher kafkaPublisher;
    private static final String TOPIC_NAME = "order-create";
    private static final String RETRY_TOPIC_NAME = "order-create.kafkaconsumer.retry";
    private static final String ERROR_TOPIC_NAME = "order-create.kafkaconsumer.error";
    private static final String GROUP_ID = "KafkaOrderConsumer-GroupId";
    private final ObjectMapper objectMapper;


    public KafkaConsumer(KafkaPublisher kafkaPublisher, ObjectMapper objectMapper) {
        this.kafkaPublisher = kafkaPublisher;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {TOPIC_NAME}, groupId = GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload OrderEvent event, ConsumerRecord consumerRecord) throws Exception {
        try {
            consume(event);
        } catch (BusinessException | ValidationException e) {

        } catch (ConnectException e) {
            String value = (String) consumerRecord.value();
            JsonNode jsonNode = objectMapper.readTree(value);
            kafkaPublisher.publish(RETRY_TOPIC_NAME, jsonNode);
        }
    }


    @KafkaListener(topics = RETRY_TOPIC_NAME, groupId = GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
    public void listener2(@Payload OrderEvent event, ConsumerRecord consumerRecord) throws Exception {
        try {
            consume(event);
        } catch (BusinessException | ValidationException e) {

        } catch (ConnectException e) {
            String value = (String) consumerRecord.value();
            JsonNode jsonNode = objectMapper.readTree(value);
            kafkaPublisher.publish(ERROR_TOPIC_NAME, jsonNode);
        }
    }


    // simulate error
    private void consume(OrderEvent event) throws Exception {
        int error = ThreadLocalRandom.current().nextInt(0,10);
        //error = 9;
        if (error < 7) {
            System.out.println("I Consumed");
        } else if (error == 7) {
            throw new BusinessException("BusinessException");
        } else if (error == 8) {
            throw new ValidationException("ValidationException");
        } else {
            throw new ConnectException("Connect to database is failed");
        }
    }


}
