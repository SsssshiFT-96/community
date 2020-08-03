package com.stndorm.community.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendUpdateESMsg(ESMessage esMessage){
        amqpTemplate.convertAndSend(RabbitMQConfig.ES_EXCHANGE, RabbitMQConfig.ES_ROUTING_KEY, esMessage);
        log.info("es更新的信息已发送...问题id为："+ esMessage.getId());
    }
}
