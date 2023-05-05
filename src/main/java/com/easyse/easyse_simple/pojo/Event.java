package com.easyse.easyse_simple.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zky
 * @date: 2022/10/24
 * @description: 封装事件，用于消息通知
 */
public class Event {

    /**
     * 事件类型
     */
    private String topic;

    /**
     * 事件由谁触发
     */
    private Long userId;

    /**
     * 实体类型
     */
    private int entityType;

    /**
     *  实体 id
     */
    private Long entityId;

    /**
     * 实体的作者(该通知发送给他）
     */
    private Long entityUserId;

    /**
     * 存储未来可能需要用到的数据
     */
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Event setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Event setEntityId(Long entityId) {
        this.entityId = entityId;
        return this;
    }

    public Long getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(Long entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
