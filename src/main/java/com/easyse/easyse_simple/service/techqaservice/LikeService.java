package com.easyse.easyse_simple.service.techqaservice;

/**
 * @author: zky
 * @date: 2022/11/20
 * @description:
 */
public interface LikeService {

    void like(Long userId, int entityType, Long entityId, Long entityUserId);
    long findEntityLikeCount(int entityType, Long entityId);

    int findEntityLikeStatus(Long userId, int entityType, Long entityId);

    int findUserLikeCount(Long userId);

}
