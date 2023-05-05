package com.easyse.easyse_simple.service.techqaservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyse.easyse_simple.pojo.DO.techqa.Comment;

import java.util.List;

/**
* @author 25405
* @description 针对表【share_comment】的数据库操作Service
* @createDate 2022-11-20 14:29:42
*/
public interface CommentService extends IService<Comment> {

    List<Comment> findCommentByEntity(int entityType, Long entityId, int offset, int limit);

    int addComment(Comment comment);

    int deleteComment(Long id);

    int findCommentCount(int entityType, Long entityId);

    int findCommentRows();

    List<Comment> findComments(Long topicId, int offset, int limit, int orderMode);

}
