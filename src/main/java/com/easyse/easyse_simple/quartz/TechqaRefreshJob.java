package com.easyse.easyse_simple.quartz;

import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.easyse.easyse_simple.constants.Constants;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.service.ElasticsearchService;
import com.easyse.easyse_simple.service.techqaservice.LikeService;
import com.easyse.easyse_simple.service.techqaservice.TechqaService;
import com.easyse.easyse_simple.utils.RedisCache;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.easyse.easyse_simple.constants.TechqaConstant.ENTITY_TYPE_TECHQA;
import static com.easyse.easyse_simple.utils.RedisKeyUtil.*;

/**
 * @author: zky
 * @date: 2022/11/29
 * @description: 帖子分数计算刷新
 */
@Slf4j
@Component
public class TechqaRefreshJob implements Job, Constants {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TechqaService techqaService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private RedisCache redisCache;

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
        String redisKey = RedisKeyUtil.getTechqaScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() == 0) {
            log.info("[任务取消] 没有需要刷新的技术问答");
            return ;
        }

        log.info("[任务开始] 正在刷新技术问答分数: " + operations.size());
        while (operations.size() > 0) {
            this.refresh((Long) operations.pop());
        }
        log.info("[任务结束] 技术问答分数刷新完毕");
    }

    /**
     * 刷新技术问答分数
     * @param techqaId
     */
    public void refresh(long techqaId) {
        Techqa techqa = techqaService.getById(techqaId);

        if (techqa == null) {
            log.error("该技术问答不存在: id = " + techqaId);
            return ;
        }

        // 是否加精
        boolean wonderful = techqa.getStatus() == 1;
        // 评论数量
        int commentCount = techqa.getCommentAmount();
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_TECHQA, techqaId);
        // 访问数量
//        int visitCount = (Integer) redisCache.getCacheObject(RedisKeyUtil.getVisitTechqa(techqa.getId()));
        // 计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2 /*+ visitCount;*/;
        // 分数 = 权重 + 发帖距离天数
        double score = Math.log10(Math.max(w, 1))
                + (techqa.getGmtCreate().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        // 日榜分数
        redisCache.zAdd(RedisKeyUtil.getDaliyScore(), techqaId, score);
        // 更新周榜
        weekRank();
        // 更新月榜
        monthRank();
        // 更新技术问答数据库分数
        techqaService.updateScore(techqaId, score);
        // 同步搜索数据
        techqa.setScore(score);
//        elasticsearchService.saveTechqa(techqa);

    }

    /**
     * 更新周榜
     */
    public void weekRank(){
        //当前日期所在周的第一天
        Calendar calendar = CalendarUtil.calendar();
        LocalDateTime weekBeginDay = CalendarUtil.toLocalDateTime(CalendarUtil.beginOfWeek(calendar));
        String weekBeginDayStr = LocalDateTimeUtil.format(weekBeginDay, "yyyy-MM-dd");
        String weekRankKey = weekBeginDayStr; //周榜缓存key yyyy-MM-dd:weekRank
        LocalDateTime weekNextDay = weekBeginDay;
        String weekNextDayStr = weekBeginDayStr; //周榜初始下一天为一周的第一天
        //计算周榜数据 周榜数据为 当前日期所在周的第一天至当前日期的数据 周榜数据需要累加的key集合
        ArrayList<String> weekKeyList = new ArrayList<>();
        //计算周榜数据
        while (true) {
            // 下一天为当天跳出循环，不存入需要计算的周榜key集合，直接在操作方法中添加当天key
            if (weekNextDayStr.equals(TODAY)) {
                break;
            }
            weekKeyList.add(weekNextDayStr+":rank");
            //在一周第一天的基础上加一天 至到日期与今天相同为周榜到今天为止的数据
            weekNextDay = weekNextDay.plusDays(1);
            weekNextDayStr = LocalDateTimeUtil.format(weekNextDay,"yyyy-MM-dd");
        }
        //循环结束后，集合中的数据为一周第一天到今天前一天的日榜key
        ZSetOperations<String, Object> zsetOperation = redisTemplate.opsForZSet();
        //对集合及今天的日榜数据进行合集计算获取周榜数据
        zsetOperation.unionAndStore(TODAY+":rank",weekKeyList,WEEK_RANK + weekRankKey);
    }

    /**
     * 更新月榜
     */
    public void monthRank(){
        //当前日期所在月的第一天
        LocalDateTime monthBeginDay = CalendarUtil.toLocalDateTime(CalendarUtil.beginOfMonth(CalendarUtil.calendar()));
        String monthBeginDayStr = LocalDateTimeUtil.format(monthBeginDay, "yyyy-MM-dd");
        String monthRankKey = monthBeginDayStr; //月榜缓存key yyyy-MM-dd:monthRank
        LocalDateTime monthNextDay = monthBeginDay;
        String monthNextDayStr = monthBeginDayStr;
        //月榜数据需要累加的key集合
        ArrayList<String> monthKeyList = new ArrayList<>();
        while (true) {

            //下一天为当天，或者是当月的最后一天跳出循环（解决today日期，循环会将下月的日榜数据也统计，正确的是统计到该月月底的日榜数据）
            if (monthNextDayStr.equals(TODAY)) {
                break;
            }
            monthKeyList.add(monthNextDayStr+":rank");
            //在一月第一天的基础上加一天，至到日期与今天相同或者与当月最后一天数据相同为月榜到今天为止的数据
            monthNextDay = monthNextDay.plusDays(1);
            monthNextDayStr = LocalDateTimeUtil.format(monthNextDay, "yyyy-MM-dd");
        }
        ZSetOperations<String, Object> zsetOperation = redisTemplate.opsForZSet();
        zsetOperation.unionAndStore(TODAY+":rank", monthKeyList,MONTH_RANK + monthRankKey);
    }
}
