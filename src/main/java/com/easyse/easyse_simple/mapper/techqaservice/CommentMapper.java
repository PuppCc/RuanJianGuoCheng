package com.easyse.easyse_simple.mapper.techqaservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.techqa.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_comment】的数据库操作Mapper
* @createDate 2022-11-20 14:29:42
* @Entity com.easyse.servicetechqa.Comment
*/
@Mapper
@Component
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectCommentByEntity(int entityType, Long entityId, int offset, int limit);

    int selectCountByEntity(int entityType, Long entityId);

    List<Comment> selectCommentByUserId(Long userId, int offset, int limit);

    int selectCommentCountByUserId(Long userId);

    int deleteByIdInt(Long id);

    int findCommentRows();

    List<Comment> findComments(Long topicId, int offset, int limit, int orderMode);

}




