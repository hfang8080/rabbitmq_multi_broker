package com.kael.example.subscriber;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Subscriber {
    @RabbitListener(queues ="q1",
            containerFactory = "factory1")
    public void listen1(String in) {
        System.out.println("listen 1");
        if (new Random().nextInt(10) > 0) {
            throw new RuntimeException("Error");
        }
        System.out.println(in);
    }

    @RabbitListener(queues ="q2", containerFactory = "factory2")
    public void listen2(String in) {
        System.out.println("listen 2");
        System.out.println(in);
    }

}
