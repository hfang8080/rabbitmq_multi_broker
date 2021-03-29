package com.kael.example.publisher;

import com.google.common.collect.ImmutableMap;
import org.springframework.amqp.rabbit.annotation.MultiRabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactoryContextWrapper;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.annotation.Bean;

public class PublisherConfig {

    @Bean
    CachingConnectionFactory cf1() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    CachingConnectionFactory cf2() {
        return new CachingConnectionFactory("otherHost");
    }

    @Bean
    SimpleRoutingConnectionFactory rcf(CachingConnectionFactory cf1, CachingConnectionFactory cf2) {

        SimpleRoutingConnectionFactory rcf = new SimpleRoutingConnectionFactory();
        rcf.setDefaultTargetConnectionFactory(cf1);
        rcf.setTargetConnectionFactories(ImmutableMap.of("one", cf1, "two", cf2));
        return rcf;
    }

    @Bean("factory1-admin")
    RabbitAdmin admin1(CachingConnectionFactory cf1) {
        return new RabbitAdmin(cf1);
    }

    @Bean("factory2-admin")
    RabbitAdmin admin2(CachingConnectionFactory cf2) {
        return new RabbitAdmin(cf2);
    }

    @Bean
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    @Bean
    public RabbitListenerAnnotationBeanPostProcessor postProcessor(RabbitListenerEndpointRegistry registry) {
        MultiRabbitListenerAnnotationBeanPostProcessor postProcessor
                = new MultiRabbitListenerAnnotationBeanPostProcessor();
        postProcessor.setEndpointRegistry(registry);
        postProcessor.setContainerFactoryBeanName("defaultContainerFactory");
        return postProcessor;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory factory1(CachingConnectionFactory cf1) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf1);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory factory2(CachingConnectionFactory cf2) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf2);
        return factory;
    }

//    @Bean
//    RabbitTemplate template(RoutingConnectionFactory rcf) {
//        return new RabbitTemplate(rcf);
//    }

    @Bean
    ConnectionFactoryContextWrapper wrapper(SimpleRoutingConnectionFactory rcf) {
        return new ConnectionFactoryContextWrapper(rcf);
    }
}
