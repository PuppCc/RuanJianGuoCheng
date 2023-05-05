package com.easyse.easyse_simple.service.techqaservice;


import com.easyse.easyse_simple.pojo.DO.techqa.TechqaTopic;
import com.github.jeffreyning.mybatisplus.service.IMppService;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_techqa_topic】的数据库操作Service
* @createDate 2022-11-26 20:52:20
*/
public interface TechqaTopicService extends IMppService<TechqaTopic> {

    List<Long> getTopicIds(Long techqaId);

}
