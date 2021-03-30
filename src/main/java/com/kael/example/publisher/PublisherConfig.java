package com.kael.example.publisher;

import com.google.common.collect.ImmutableMap;
import com.kael.example.subscriber.FirstConfig;
import com.kael.example.subscriber.SecondConfig;
import com.kael.example.subscriber.SubscriberConfig;
import org.springframework.amqp.rabbit.annotation.MultiRabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactoryContextWrapper;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SubscriberConfig.class)
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class PublisherConfig {
//    @Autowired
//    FirstConfig firstConfig;
//    @Autowired
//    SecondConfig secondConfig;
//
//    @Bean("cf1")
//    CachingConnectionFactory cf1() {
//        CachingConnectionFactory cf1 = new CachingConnectionFactory();
//        cf1.setHost(firstConfig.getHost());
//        cf1.setPort(firstConfig.getPort());
//        cf1.setUsername(firstConfig.getUsername());
//        cf1.setPassword(firstConfig.getPassword());
//        cf1.setVirtualHost(firstConfig.getVhost());
//        return cf1;
//    }
//
//    @Bean("cf2")
//    CachingConnectionFactory cf2() {
//        CachingConnectionFactory cf2 = new CachingConnectionFactory();
//        cf2.setHost(secondConfig.getHost());
//        cf2.setPort(secondConfig.getPort());
//        cf2.setUsername(secondConfig.getUsername());
//        cf2.setPassword(secondConfig.getPassword());
//        cf2.setVirtualHost(secondConfig.getVhost());
//        return cf2;
//    }

//    @Bean("crf")
//    SimpleRoutingConnectionFactory rcf(
//            @Qualifier("cf1") CachingConnectionFactory cf1,
//            @Qualifier("cf2") CachingConnectionFactory cf2) {
//        SimpleRoutingConnectionFactory rcf = new SimpleRoutingConnectionFactory();
//        rcf.setDefaultTargetConnectionFactory(cf1);
//        rcf.setTargetConnectionFactories(ImmutableMap.of("one", cf1, "two", cf2));
//        return rcf;
//    }

//    @Bean("factory1-admin")
//    RabbitAdmin admin1(@Qualifier("cf1") CachingConnectionFactory cf1) {
//        return new RabbitAdmin(cf1);
//    }
//
//    @Bean("factory2-admin")
//    RabbitAdmin admin2(@Qualifier("cf2") CachingConnectionFactory cf2) {
//        return new RabbitAdmin(cf2);
//    }
//
//    @Bean
//    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
//        return new RabbitListenerEndpointRegistry();
//    }
//
//    @Bean
//    public RabbitListenerAnnotationBeanPostProcessor postProcessor(RabbitListenerEndpointRegistry registry) {
//        MultiRabbitListenerAnnotationBeanPostProcessor postProcessor
//                = new MultiRabbitListenerAnnotationBeanPostProcessor();
//        postProcessor.setEndpointRegistry(registry);
//        postProcessor.setContainerFactoryBeanName("defaultContainerFactory");
//        return postProcessor;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory factory1(@Qualifier("cf1") CachingConnectionFactory cf1) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(cf1);
//        return factory;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory factory2(@Qualifier("cf2") CachingConnectionFactory cf2) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(cf2);
//        return factory;
//    }

    @Bean("template1")
    RabbitTemplate template(@Qualifier("cf1") ConnectionFactory cf1) {
        return new RabbitTemplate(cf1);
    }

    @Bean("template2")
    RabbitTemplate template2(@Qualifier("cf2") ConnectionFactory cf2) {
        return new RabbitTemplate(cf2);
    }

}
