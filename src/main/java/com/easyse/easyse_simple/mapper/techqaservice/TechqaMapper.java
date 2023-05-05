package com.easyse.easyse_simple.mapper.techqaservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_techqa(技术问答表
)】的数据库操作Mapper
* @createDate 2022-10-23 20:02:13
* @Entity com.easyse.Techqa
*/
@Mapper
@Component
public interface TechqaMapper extends BaseMapper<Techqa> {

    /**
     * 更新技术问答状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, double status);

    /**
     * 更新帖子类型
     * @param id
     * @param type
     */
    void updateType(Long id, int type);

    /**
     * 分页查询讨论帖信息
     *
     * @param userId 当传入的 userId = 0 时查找所有用户的帖子
     *               当传入的 userId != 0 时，查找该指定用户的帖子
     * @param offset 每页的起始索引
     * @param limit  每页显示多少条数据
     * @param orderMode  排行模式(若传入 1, 则按照热度来排序)
     * @return
     */
    List<Techqa> selectTechqas(Long userId, int offset, int limit, int orderMode);

    /**
     * 查询讨论贴的个数
     * @param userId 当传入的 userId = 0 时计算所有用户的帖子总数
     *               当传入的 userId ！= 0 时计算该指定用户的帖子总数
     * @return
     */
    int selectTechqaRows(@Param("userId") Long userId);

    /**
     * 修改评论数量
     * @param id
     * @param commentCount
     * @return
     */
    int updateCommentCount(Long id, int commentCount);

    /**
     * 更新技术问答分数
     * @param id
     * @param score
     */
    int updateScore(Long id, double score);

}





