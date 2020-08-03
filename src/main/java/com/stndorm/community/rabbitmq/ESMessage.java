package com.stndorm.community.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ESMessage implements Serializable {

    //设置两种消息类型
    public final static String CREATE_OR_UPDATE = "create_update";
    public final static String REMOVE = "remove";

    private Integer id;
    private String type;
}
