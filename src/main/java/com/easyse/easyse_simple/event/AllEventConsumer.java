package com.easyse.easyse_simple.event;

import com.alibaba.fastjson.JSONObject;
import com.easyse.easyse_simple.constants.KafkaConstant;
import com.easyse.easyse_simple.constants.UserConstants;
import com.easyse.easyse_simple.pojo.DO.person.Message;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.pojo.Event;
import com.easyse.easyse_simple.service.ElasticsearchService;
import com.easyse.easyse_simple.service.kafkaserivce.MessageService;
import com.easyse.easyse_simple.service.techqaservice.TechqaService;
import com.easyse.easyse_simple.utils.LRU;
import com.easyse.easyse_simple.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zky
 * @date: 2022/10/24
 * @description: 消费消费者
 */
@Component
@Slf4j
public class AllEventConsumer implements KafkaConstant, UserConstants {
    
    @Autowired
    private MessageService messageService;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    TechqaService techqaService;

    @Autowired
    UserHolder userHolder;

    /**
     * 消费评论、点赞、关注事件
     * @param record
     */
    @KafkaListener(topics = {TOPIC_COMMNET, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return ;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误");
            return ;
        }

        // 发送系统通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        if (!event.getData().isEmpty()) { // 存储 Event 中的 Data
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));

        messageService.save(message);

    }

    /**
     * 消费发布技术问答事件
     */
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return ;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误");
            return ;
        }

        Techqa techqa = techqaService.getById(event.getEntityId());
        elasticsearchService.saveTechqa(techqa);

    }

    /**
     * 消费删除技术问答事件
     */
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return ;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误");
            return ;
        }

        elasticsearchService.deleteTechqa(event.getEntityId());

    }


    /**
     * flume测试，
     */
    @KafkaListener(topics = "test1")
    public void flumeTest(ConsumerRecord record){

        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return ;
        }

        System.out.println(record.value());

    }

    @KafkaListener(topics = TOPIC_SEARCH)
    public void Search(ConsumerRecord record){

        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return ;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        Long userId = event.getUserId();
        String keyword = (String)event.getData().get("keyword");
        // 记录到LRU缓存中
        LRU hobby = userHolder.getHobby(userId);
        hobby.set(keyword);

        System.out.println(record.value());

    }

}
