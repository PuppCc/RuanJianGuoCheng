package com.easyse.easyse_simple.service.kafkaserivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.kafkaservice.MessageMapper;
import com.easyse.easyse_simple.pojo.DO.person.Message;
import com.easyse.easyse_simple.service.kafkaserivce.MessageService;
import com.easyse.easyse_simple.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
* @author 25405
* @description 针对表【person_message】的数据库操作Service实现
* @createDate 2022-10-24 11:05:24
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Override
    public int findConversationCout(Long userId) {
        return messageMapper.selectConversationCount(userId);
    }

    /**
     * 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findConversations(Long userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId
     * @return
     */
    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    /**
     * 查询未读私信的数量
     * @param userId
     * @param conversationId
     * @return
     */
    public int findLetterUnreadCount(Long userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    /**
     * 读取私信(将私信状态设置为已读)
     * @param ids
     * @return
     */
    public int readMessage(List<Long> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

    /**
     * 添加一条私信
     * @param message
     * @return
     */
    public int addMessage(Message message) {
        // 转义 HTML 标签
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        // 过滤敏感词
        message.setContent(sensitiveFilter.filter(message.getContent()));

        return messageMapper.insertMessage(message);
    }

    /**
     * 查询某个主题下最新的系统通知
     * @param userId
     * @param topic
     * @return
     */
    public Message findLatestNotice(Long userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    /**
     * 查询某个主题下包含的系统通知数量
     * @param userId
     * @param topic
     * @return
     */
    public int findNoticeCount(Long userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    /**
     * 查询未读的系统通知数量
     * @param userId
     * @param topic
     * @return
     */
    public int findNoticeUnReadCount(Long userId, String topic) {
        return messageMapper.selectNoticeUnReadCount(userId, topic);
    }

    /**
     * 查询某个主题所包含的通知列表
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findNotices(Long userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }

}




