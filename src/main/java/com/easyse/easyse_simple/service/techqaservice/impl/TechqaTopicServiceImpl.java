package com.easyse.easyse_simple.service.techqaservice.impl;

import com.easyse.easyse_simple.mapper.techqaservice.TechqaTopicMapper;
import com.easyse.easyse_simple.pojo.DO.techqa.TechqaTopic;
import com.easyse.easyse_simple.service.techqaservice.TechqaTopicService;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_techqa_topic】的数据库操作Service实现
* @createDate 2022-11-26 20:52:20
*/
@Service
public class TechqaTopicServiceImpl extends MppServiceImpl<TechqaTopicMapper, TechqaTopic>
    implements TechqaTopicService {
    @Autowired
    TechqaTopicMapper topicMapper;

    @Override
    public List<Long> getTopicIds(Long techqaId) {
        return topicMapper.getTopicIds(techqaId);
    }
}




