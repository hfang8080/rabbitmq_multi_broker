package com.kael.example.subscriber;

import com.google.common.collect.ImmutableMap;
import com.kael.example.exception.CustomFatalExceptionStrategy;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.MultiRabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactoryContextWrapper;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Configuration
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class SubscriberConfig {

    @Bean("cf1")
    CachingConnectionFactory cf1(FirstConfig firstConfig) {
        CachingConnectionFactory cf1 = new CachingConnectionFactory();
        cf1.setHost(firstConfig.getHost());
        cf1.setPort(firstConfig.getPort());
        cf1.setUsername(firstConfig.getUsername());
        cf1.setPassword(firstConfig.getPassword());
        cf1.setVirtualHost(firstConfig.getVhost());
        return cf1;
    }

    @Bean("cf2")
    CachingConnectionFactory cf2(SecondConfig secondConfig) {
        CachingConnectionFactory cf2 = new CachingConnectionFactory();
        cf2.setHost(secondConfig.getHost());
        cf2.setPort(secondConfig.getPort());
        cf2.setUsername(secondConfig.getUsername());
        cf2.setPassword(secondConfig.getPassword());
        cf2.setVirtualHost(secondConfig.getVhost());
        return cf2;
    }

//    @Bean("crf")
//    SimpleRoutingConnectionFactory rcf(
//            @Qualifier("cf1") CachingConnectionFactory cf1,
//            @Qualifier("cf2") CachingConnectionFactory cf2) {
//        SimpleRoutingConnectionFactory rcf = new SimpleRoutingConnectionFactory();
//        rcf.setDefaultTargetConnectionFactory(cf1);
//        rcf.setTargetConnectionFactories(ImmutableMap.of("one", cf1, "two", cf2));
//        return rcf;
//    }

    @Bean("factory1-admin")
    RabbitAdmin admin1(@Qualifier("cf1") CachingConnectionFactory cf1) {
        return new RabbitAdmin(cf1);
    }

    @Bean("factory2-admin")
    RabbitAdmin admin2(@Qualifier("cf2") CachingConnectionFactory cf2) {
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
    public SimpleRabbitListenerContainerFactory factory1(@Qualifier("cf1") CachingConnectionFactory cf1) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setErrorHandler(errorHandler());
        factory.setConnectionFactory(cf1);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory factory2(@Qualifier("cf2") CachingConnectionFactory cf2) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setErrorHandler(errorHandler());
        factory.setConnectionFactory(cf2);
        return factory;
    }

    @Bean
    public FanoutExchange q1Exchange(@Qualifier("factory1-admin") RabbitAdmin admin) {
        FanoutExchange exchange = new FanoutExchange("exchange");
        exchange.setAdminsThatShouldDeclare(admin);
        //exchange.setIgnoreDeclarationExceptions(false);
        return exchange;
    }

    @Bean
    public FanoutExchange q1DLQExchange(@Qualifier("factory1-admin") RabbitAdmin admin) {
        FanoutExchange exchange = new FanoutExchange("q1-dlq-exchange");
        exchange.setAdminsThatShouldDeclare(admin);
        //exchange.setIgnoreDeclarationExceptions(false);
        return exchange;
    }

    @Bean
    public FanoutExchange q2DLQExchange(@Qualifier("factory2-admin") RabbitAdmin admin2) {
        FanoutExchange exchange = new FanoutExchange("q2-dlq-exchange");
        exchange.setAdminsThatShouldDeclare(admin2);
        //exchange.setIgnoreDeclarationExceptions(false);
        return exchange;
    }

    @Bean
    public FanoutExchange q2Exchange(@Qualifier("factory2-admin") RabbitAdmin admin2) {
        FanoutExchange exchange = new FanoutExchange("exchange2");
        exchange.setAdminsThatShouldDeclare(admin2);
        //exchange.setIgnoreDeclarationExceptions(false);
        return exchange;
    }

    @Bean
    public Queue dlqQueue1(@Qualifier("factory1-admin") RabbitAdmin admin) {
        Queue queue = QueueBuilder.durable("q1-dlq")
                .build();
        queue.setAdminsThatShouldDeclare(admin);
//        queue.setIgnoreDeclarationExceptions(false);
        return queue;
    }

    @Bean
    public Binding q1Binding(@Qualifier("factory1-admin") RabbitAdmin admin) {
        Binding binding = BindingBuilder.bind(queue1(admin))
                .to(q1Exchange(admin));
        binding.setAdminsThatShouldDeclare(admin);
//        binding.setIgnoreDeclarationExceptions(false);
        return binding;
    }

    @Bean
    public Binding q2Binding(@Qualifier("factory2-admin") RabbitAdmin admin) {
        Binding binding = BindingBuilder.bind(queue2(admin))
                .to(q2Exchange(admin));
        binding.setAdminsThatShouldDeclare(admin);
//        binding.setIgnoreDeclarationExceptions(false);
        return binding;
    }

    @Bean
    public Binding dlq1Binding(@Qualifier("factory1-admin") RabbitAdmin admin) {
        Binding binding = BindingBuilder.bind(dlqQueue1(admin))
                .to(q1DLQExchange(admin));
        binding.setAdminsThatShouldDeclare(admin);
//        binding.setIgnoreDeclarationExceptions(false);
        return binding;
    }

    @Bean
    public Binding dlq2Binding(@Qualifier("factory2-admin") RabbitAdmin admin) {
        Binding binding = BindingBuilder.bind(dlqQueue2(admin))
                .to(q2DLQExchange(admin));
        binding.setAdminsThatShouldDeclare(admin);
//        binding.setIgnoreDeclarationExceptions(false);
        return binding;
    }

    @Bean
    public Queue dlqQueue2(@Qualifier("factory2-admin") RabbitAdmin admin2) {
        Queue queue = QueueBuilder.durable("q2-dlq")
                .build();
        queue.setAdminsThatShouldDeclare(admin2);
//        queue.setIgnoreDeclarationExceptions(false);
        return queue;
    }

    @Bean
    public Queue queue1(@Qualifier("factory1-admin") RabbitAdmin admin) {
        Queue q1 = QueueBuilder.durable("q1")
                .withArgument("x-dead-letter-exchange", "q1-dlq-exchange")
//                .deadLetterExchange()
                .build();
        q1.setAdminsThatShouldDeclare(admin);
//        q1.setIgnoreDeclarationExceptions(false);
        return q1;
    }

    @Bean
    public Queue queue2(@Qualifier("factory2-admin") RabbitAdmin admin2) {
        Queue q2 = QueueBuilder.durable("q2")
                .deadLetterExchange("q2-dlq-exchange")
                .build();
        q2.setAdminsThatShouldDeclare(admin2);
//        q2.setIgnoreDeclarationExceptions(false);
        return q2;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(customExceptionStrategy());
    }

    @Bean
    FatalExceptionStrategy customExceptionStrategy() {
        return new CustomFatalExceptionStrategy();
    }


//    @Bean
//    RabbitTemplate template(RoutingConnectionFactory rcf) {
//        return new RabbitTemplate(rcf);
//    }

//    @Bean
//    ConnectionFactoryContextWrapper wrapper(@Qualifier("crf") SimpleRoutingConnectionFactory rcf) {
//        return new ConnectionFactoryContextWrapper(rcf);
//    }
}
