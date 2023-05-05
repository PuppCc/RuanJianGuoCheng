package com.easyse.easyse_simple.service.techqaservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_techqa(技术问答表
)】的数据库操作Service
* @createDate 2022-10-23 20:02:13
*/
public interface TechqaService extends IService<Techqa> {

    void updateStatus(Long id, int status);

    void updateType(Long id, int statue);

    int addTechqa(Techqa techqa);

    int findTechqaRows (Long userId);

    List<Techqa> findTechqas (Long userId, int offset, int limit, int orderMode);

    int updateCommentCount(Long id, int commentCount);

    int updateScore(Long id, double score);



}
