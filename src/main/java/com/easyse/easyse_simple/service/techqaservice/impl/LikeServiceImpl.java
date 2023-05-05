package com.easyse.easyse_simple.service.techqaservice.impl;

import com.easyse.easyse_simple.service.techqaservice.LikeService;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author: zky
 * @date: 2022/11/20
 * @description:
 */
@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param userId 点赞的用户 id
     * @param entityType
     * @param entityId
     * @param entityUserId 被赞的帖子/评论的作者 id
     */
    public void like(Long userId, int entityType, Long entityId, Long entityUserId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                // 判断用户是否已经点过赞了
                boolean isMember = redisOperations.opsForSet().isMember(entityLikeKey, userId);

                redisOperations.multi(); // 开启事务

                if (isMember) {
                    // 如果用户已经点过赞，点第二次则取消赞
                    redisOperations.opsForSet().remove(entityLikeKey, userId);
                    redisOperations.opsForValue().decrement(userLikeKey);
                }
                else {
                    redisTemplate.opsForSet().add(entityLikeKey, userId);
                    redisOperations.opsForValue().increment(userLikeKey);
                }

                return redisOperations.exec(); // 提交事务
            }
        });
    }

    /**
     * 查询某实体被点赞的数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long findEntityLikeCount(int entityType, Long entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * 查询某个用户对某个实体的点赞状态（是否已赞）
     * @param userId
     * @param entityType
     * @param entityId
     * @return 1:已赞，0:未赞
     */
    public int findEntityLikeStatus(Long userId, int entityType, Long entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    /**
     * 查询某个用户获得赞数量
     * @param userId
     * @return
     */
    public int findUserLikeCount(Long userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }
}
