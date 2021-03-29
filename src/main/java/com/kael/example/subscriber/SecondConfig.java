package com.kael.example.subscriber;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "rabbitmq.second")
@Configuration
public class SecondConfig extends BaseConfig {
}
