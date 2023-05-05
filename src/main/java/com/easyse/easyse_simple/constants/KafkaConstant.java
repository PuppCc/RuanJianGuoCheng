package com.easyse.easyse_simple.constants;

/**
 * @author: zky
 * @date: 2022/10/24
 * @description: kafka常量
 */
public interface KafkaConstant {
    // Kafka 主题：评论
    String TOPIC_COMMNET = "comment";

    // Kafka 主题：点赞
    String TOPIC_LIKE = "like";

    // Kafka 主题：关注
    String TOPIC_FOLLOW = "follow";

    // Kafka 主题：发布技术问答
    String TOPIC_PUBLISH = "publish";

    // Kafka 主题：删除技术问答
    String TOPIC_DELETE = "delete";

    // Kafka 主题：搜索
    String TOPIC_SEARCH = "delete";
}
