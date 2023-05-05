package com.easyse.easyse_simple.service.techqaservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.techqaservice.CommentMapper;
import com.easyse.easyse_simple.pojo.DO.techqa.Comment;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.service.ElasticsearchService;
import com.easyse.easyse_simple.service.techqaservice.CommentService;
import com.easyse.easyse_simple.service.techqaservice.TechqaService;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import com.easyse.easyse_simple.utils.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

import static com.easyse.easyse_simple.constants.TechqaConstant.ENTITY_TYPE_TECHQA;


/**
* @author 25405
* @description 针对表【share_comment】的数据库操作Service实现
* @createDate 2022-11-20 14:29:42
*/
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private TechqaService techqaService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据 id 查询评论
     * @param id
     * @return
     */
    public Comment findCommentById(Long id) {
        return commentMapper.selectById(id);
    }


    /**
     * 根据评论目标（类别、id）对评论进行分页查询
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    public List<Comment> findCommentByEntity(int entityType, Long entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    /**
     * 查询评论的数量
     * @param entityType
     * @param entityId
     * @return
     */
    public int findCommentCount(int entityType, Long entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    /**
     * 分页查询某个用户的评论/回复列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Comment> findCommentByUserId(Long userId, int offset, int limit) {
        return commentMapper.selectCommentByUserId(userId, offset, limit);
    }

    /**
     * 查询某个用户的评论/回复数量
     * @param userId
     * @return
     */
    public int findCommentCountByUserId(Long userId) {
        return commentMapper.selectCommentCountByUserId(userId);
    }

    /**
     * 添加评论（需要事务管理）
     * @param comment
     * @return
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // Html 标签转义
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));

        // TODO 敏感词过滤
        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        // 添加评论
        int rows = commentMapper.insert(comment);

        // 更新帖子的评论数量
        if (comment.getEntityType() == ENTITY_TYPE_TECHQA) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            techqaService.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }

    @Override
    public int deleteComment(Long id) {
        return commentMapper.deleteByIdInt(id);
    }

    @Override
    public int findCommentRows() {
        return commentMapper.findCommentRows();
    }

    @Override
    public List<Comment> findComments(Long topicId, int offset, int limit, int orderMode) {
        return commentMapper.findComments(topicId, offset, limit, orderMode);
    }

    /**
     * 刷新技术问答评论数
     */
    @Scheduled(cron = "0 */5 * * * ?")
    private void executerefresh() {
        String redisKey = RedisKeyUtil.getTechqaCommentKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);
        if (operations.size() == 0) {
            log.info("[评论刷新任务取消] 没有需要刷新的技术问答");
            return ;
        }
        log.info("[评论刷新任务开始] 正在刷新帖子评论数: " + operations.size());
        while (operations.size() > 0) {
            this.refresh((Integer) operations.pop());
        }
        log.info("[任务结束] 评论刷新任务刷新完毕");



    }

    /**
     * 刷新技术问答评论数
     * @param postId
     */
    private void refresh(long postId) {
        Techqa techqa = techqaService.getById(postId);

        if (techqa == null) {
            log.error("该技术问答不存在: id = " + postId);
            return ;
        }

        Integer commentAmount = techqa.getCommentAmount();
        commentAmount++;
        // 更新技术问答分数
        techqaService.updateCommentCount(postId, commentAmount);
        // 同步搜索数据
        techqa.setCommentAmount(commentAmount);
        elasticsearchService.saveTechqa(techqa);
    }
}




