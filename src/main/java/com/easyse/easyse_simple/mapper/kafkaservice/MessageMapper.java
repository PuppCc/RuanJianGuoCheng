package com.easyse.easyse_simple.mapper.kafkaservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.person.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author 25405
* @description 针对表【person_message】的数据库操作Mapper
* @createDate 2022-10-24 11:05:24
* @Entity com.easyse.kafka.Message
*/
@Mapper
@Component
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * @param userId 用户 id
     * @param offset 每页的起始索引
     * @param limit 每页显示多少条数据
     * @return
     */
    List<Message> selectConversations(Long userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量
     * @param userId
     * @return
     */
    int selectConversationCount(Long userId);


    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId
     * @return
     */
    int selectLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     * @param userId
     * @param conversationId conversationId = null, 则查询该用户所有会话的未读私信数量
     *                        conversationId != null, 则查询该用户某个会话的未读私信数量
     * @return
     */
    int selectLetterUnreadCount(Long userId, String conversationId);

    /**
     * 修改消息的状态
     * @param ids
     * @param status
     * @return
     */
    int updateStatus(List<Long> ids, int status);

    /**
     * 新增一条私信
     * @param message
     * @return
     */
    int insertMessage(Message message);

    /**
     * 查询某个主题下最新的通知
     * @param userId
     * @param topic
     * @return
     */
    Message selectLatestNotice(Long userId, String topic);

    /**
     * 查询某个主题下包含的系统通知数量
     * @param userId
     * @param topic
     * @return
     */
    int selectNoticeCount(Long userId, String topic);

    /**
     * 查询未读的系统通知数量
     * @param userId
     * @param topic
     * @return
     */
    int selectNoticeUnReadCount(Long userId, String topic);

    /**
     * 查询某个主题所包含的通知列表
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectNotices(Long userId, String topic, int offset, int limit);


}




