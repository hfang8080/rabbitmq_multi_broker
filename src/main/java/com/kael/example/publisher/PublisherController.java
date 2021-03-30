// Copyright 2021 ALO7 Inc. All rights reserved.

package com.kael.example.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;

/**
 * @author Kael He (kael.he@alo7.com)
 */
@RestController
@RequestMapping("/v1")
public class PublisherController {
    @Autowired
    @Qualifier("template1")
    RabbitTemplate template;

    @Autowired
    @Qualifier("template2")
    RabbitTemplate template2;

    @GetMapping("/send")
    public String send(@RequestParam("a") String a) {
        if (!a.equals("exit")) {
            template.convertAndSend("exchange", "", a);
            template2.convertAndSend("exchange2", "", a);
        }
        return "success";
    }
}
