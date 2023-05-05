package com.easyse.easyse_simple.mapper.techqaservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.techqa.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_topic(话题表)】的数据库操作Mapper
* @createDate 2022-10-23 20:02:13
* @Entity com.easyse.Topic
*/
@Mapper
@Component
public interface TopicMapper extends BaseMapper<Topic> {

    List<Topic> getTopics(Long topicId, int offset, int limit, int orderMode);

    int getTopicRows();

}




