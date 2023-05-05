package com.easyse.easyse_simple.service.techqaservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyse.easyse_simple.pojo.DO.techqa.Comment;
import com.easyse.easyse_simple.pojo.DO.techqa.Topic;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_topic(话题表)】的数据库操作Service
* @createDate 2022-10-23 20:02:13
*/
public interface TopicService extends IService<Topic> {

    List<Topic> getTopics(Long topicId, int offset, int limit, int orderMode);

    int getTopicRows();



}
