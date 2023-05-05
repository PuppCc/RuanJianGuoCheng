package com.easyse.easyse_simple.mapper.techqaservice;

import com.easyse.easyse_simple.pojo.DO.techqa.TechqaTopic;
import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_techqa_topic】的数据库操作Mapper
* @createDate 2022-11-26 20:52:20
* @Entity com.easyse.servicetechqa.TechqaTopic
*/
@Mapper
@Component
public interface TechqaTopicMapper extends MppBaseMapper<TechqaTopic> {
    List<Long> getTopicIds(Long techqaId);
}




