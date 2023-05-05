package com.easyse.easyse_simple.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author zky
 * @date: 2022/10/15
 * @description: redis相关key
 */
public class RedisKeyUtil {

    /**
     * 分隔符
     */
    public static final String SPLIT = ":";

    /**
     * redis用户前缀
     */
    public static final String PREFIX_USER = "user:";

    /**
     * 实体的获赞
     */
    public static final String PREFIX_ENTITY_LIKE = "like:entity";

    /**
     * 用户的获赞
     */
    public static final String PREFIX_USER_LIKE = "like:user";

    /**
     * 关注者
     */
    public static final String PREFIX_FOLLOWER = "follower";

    /**
     * 关注的目标
     */
    public static final String PREFIX_FOLLOWEE = "followee";

    /**
     * 独立访客
     */
    public static final String PREFIX_UV = "uv";

    /**
     * 日活跃用户
     */
    public static final String PREFIX_DAU = "dau";

    /**
     * 统计帖子分数
     */
    public static final String PREFIX_AQ = "AQ";

    /**
     * 用于统计帖子分数
     */
    public static final String PREFIX_COMMENT = "comment";

    /**
     * 用于记录访问次数
     */
    public static final String PREFIX_TECHQA = "visit:techqa";

    /**
     * 用于记录技术问答每日的分数
     */
    public static final String DALIY_TECHQA_SCORE = "rank";

    /**
     * 当日时间
     */
    public static final String TODAY = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    /**
     *  周榜
     */
    public static final String WEEK_RANK = "weekRank_";

    /**
     *  周榜
     */
    public static final String MONTH_RANK = "monthRank_";

    /**
     * 某个实体（技术问答、评论/回复）的获赞
     * like:entity:entityType:entityId -> set(userId)
     * 谁给这个实体点了赞，就将这个用户的id存到这个实体对应的集合里
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getEntityLikeKey(int entityType, Long entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 某个用户的获赞数量
     * like:user:userId -> int
     * @param userId 获赞用户的 id
     * @return
     */
    public static String getUserLikeKey(Long userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * 某个用户关注的实体
     * followee:userId:entityType -> zset(entityId, now) 以当前关注的时间进行排序
     * @param userId
     * @param entityType 关注的实体类型
     * @return
     */
    public static String getFolloweeKey(Long userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 某个实体拥有的关注者
     * follower:entityType:entityId -> zset(userId, now)
     * @param entityType
     * @param entityId
     * @return
     */
     public static String getFollowerKey(int entityType, Long entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
     }

    /**
     * 用户信息
     * @param userId
     * @return
     */
    public static String getUserKey(long userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    /**
     * 单日 UV
     * @param date
     * @return
     */
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    /**
     * 区间 UV
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    /**
     * 单日 DAU
     * @param date
     * @return
     */
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    /**
     * 区间 DAU
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    /**
     * 技术问答分数
     * @return
     */
    public static String getTechqaScoreKey() {
        return PREFIX_AQ + SPLIT + "score";
    }

    /**
     * 技术评论相关
     * @return
     */
    public static String getTechqaCommentKey() {
        return PREFIX_AQ + SPLIT + "comment";
    }

    /**
     * 记录访问数
     */
    public static String getVisitTechqa(Long techqaId){
        return PREFIX_TECHQA + SPLIT +  techqaId;
    }

    /**
     * 每日活跃度分数
     */
    public static String getDaliyScore(){
        return TODAY + SPLIT +  DALIY_TECHQA_SCORE;
    }
}