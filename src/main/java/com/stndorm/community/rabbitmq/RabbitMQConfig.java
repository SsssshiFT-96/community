package com.stndorm.community.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ES_QUEUE = "es_queue";
    public static final String ES_EXCHANGE = "es_exchange";
    public static final String ES_ROUTING_KEY = "es_route_key";

    @Bean
    public Queue esQueue(){
        return new Queue(ES_QUEUE);
    }

    @Bean
    public DirectExchange esChange(){
        return new DirectExchange(ES_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue esQueue, DirectExchange esChange){
        return BindingBuilder.bind(esQueue).to(esChange).with(ES_ROUTING_KEY);
    }
}
