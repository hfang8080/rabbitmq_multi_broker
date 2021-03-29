package com.kael.example.subscriber;

import lombok.Data;

@Data
public class BaseConfig {
    private String vhost;
    private String username;
    private String password;
    private String host;
    private int port;
}
