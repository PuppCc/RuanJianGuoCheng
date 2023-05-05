package com.easyse.easyse_simple.event;

import com.alibaba.fastjson.JSONObject;
import com.easyse.easyse_simple.pojo.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: zky
 * @date: 2022/10/24
 * @description: 消费生产者
 */
@Component
@Slf4j
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;


    /**
     * 处理事件
     * @param event
     */
    public void fireEvent(Event event) {
        // 将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }




}
