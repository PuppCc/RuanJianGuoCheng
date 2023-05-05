package com.easyse.easyse_simple.quartz;

import com.easyse.easyse_simple.constants.Constants;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.service.ElasticsearchService;
import com.easyse.easyse_simple.service.techqaservice.TechqaService;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author: zky
 * @date: 2022/11/29
 * @description: 帖子分数计算刷新
 */
@Slf4j
public class CommentRefreshJob implements Job, Constants {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TechqaService techqaService;

    @Autowired
    private ElasticsearchService elasticsearchService;


    // Epoch 纪元
    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-01-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化 Epoch 纪元失败", e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
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
        log.info("[任务结束] 技术问答分数刷新完毕");
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
