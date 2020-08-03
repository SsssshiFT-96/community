package com.stndorm.community.rabbitmq;

import com.stndorm.community.elasticsearch.ESService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = RabbitMQConfig.ES_QUEUE)
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    ESService esService;

    @RabbitHandler
    public void handler(ESMessage esMessage){
//        log.info("问题的id" + esMessage.getId());
//        System.out.println(esMessage.getType());
        if(esMessage.getType().equals(ESMessage.CREATE_OR_UPDATE)){
//            System.out.println(esMessage.getType());
            esService.createOrUpdate(esMessage.getId());
        }else if(esMessage.getType().equals(ESMessage.REMOVE)){
            esService.remove(esMessage.getId());
        }
        log.info("es索引已更新...");
    }
}
