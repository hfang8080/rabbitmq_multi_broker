package com.kael.example.subscriber;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Subscriber {
    @RabbitListener(queuesToDeclare = @Queue("q1"), containerFactory = "factory1")
    public void listen1(String in) {
        System.out.println("listen 1");
        System.out.println(in);
    }

    @RabbitListener(queuesToDeclare = @Queue("q2"), containerFactory = "factory2")
    public void listen2(String in) {
        System.out.println("listen 2");
        System.out.println(in);
    }

}
