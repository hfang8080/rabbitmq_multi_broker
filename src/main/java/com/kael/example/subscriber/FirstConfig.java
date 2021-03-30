package com.kael.example.subscriber;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "rabbitmq.first")
@Configuration
public class FirstConfig extends BaseConfig {
}
