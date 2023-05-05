package com.easyse.easyse_simple.service.techqaservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.techqaservice.TopicMapper;
import com.easyse.easyse_simple.pojo.DO.techqa.Topic;
import com.easyse.easyse_simple.service.techqaservice.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_topic(话题表)】的数据库操作Service实现
* @createDate 2022-10-23 20:02:13
*/
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
    implements TopicService {

    @Autowired
    TopicMapper topicMapper;

    @Override
    public List<Topic> getTopics(Long topicId, int offset, int limit, int orderMode) {
        return topicMapper.getTopics(topicId, offset, limit, orderMode);
    }

    @Override
    public int getTopicRows() {
        return topicMapper.getTopicRows();
    }


}




