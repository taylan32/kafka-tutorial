package com.example.kafkaconsumer.model.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderEvent {
    private String id;
    private String username;
    private String price;
    private String createdTime;
}
