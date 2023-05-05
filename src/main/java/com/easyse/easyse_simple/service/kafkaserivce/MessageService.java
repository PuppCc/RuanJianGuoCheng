package com.easyse.easyse_simple.service.kafkaserivce;


import com.baomidou.mybatisplus.extension.service.IService;
import com.easyse.easyse_simple.pojo.DO.person.Message;

import java.util.List;

/**
* @author 25405
* @description 针对表【person_message】的数据库操作Service
* @createDate 2022-10-24 11:05:24
*/
public interface MessageService extends IService<Message> {

    /**
     * 查询当前用户的会话数量
     * @param userId
     * @return
     */
    int findConversationCout(Long userId);

    /**
     * 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> findConversations(Long userId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> findLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId
     * @return
     */
    int findLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     * @param userId
     * @param conversationId
     * @return
     */
    int findLetterUnreadCount(Long userId, String conversationId);

    /**
     * 读取私信(将私信状态设置为已读)
     * @param ids
     * @return
     */
    int readMessage(List<Long> ids);

    /**
     * 添加一条私信
     * @param message
     * @return
     */
    int addMessage(Message message);

    /**
     * 查询某个主题下最新的系统通知
     * @param userId
     * @param topic
     * @return
     */
    Message findLatestNotice(Long userId, String topic);

    /**
     * 查询某个主题下包含的系统通知数量
     * @param userId
     * @param topic
     * @return
     */
    int findNoticeCount(Long userId, String topic);

    /**
     * 查询未读的系统通知数量
     * @param userId
     * @param topic
     * @return
     */
    int findNoticeUnReadCount(Long userId, String topic);

    /**
     * 查询某个主题所包含的通知列表
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    List<Message> findNotices(Long userId, String topic, int offset, int limit);

}
