package com.easyse.easyse_simple.controller.techqaservice;

import com.easyse.easyse_simple.constants.Constants;
import com.easyse.easyse_simple.constants.TechqaConstant;
import com.easyse.easyse_simple.event.EventProducer;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.Event;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.techqaservice.LikeService;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import com.easyse.easyse_simple.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.easyse.easyse_simple.constants.KafkaConstant.TOPIC_LIKE;

@RestController
@RequestMapping(value = "/techqa")
@Api(tags = "点赞相关")
public class LikeController implements Constants, TechqaConstant {

    @Autowired
    private UserHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param entityType
     * @param entityId
     * @param entityUserId 赞的帖子/评论的作者 id
     * @param techqaId 帖子的 id (点赞了哪个帖子，点赞的评论属于哪个帖子，点赞的回复属于哪个帖子)
     * @return
     */
    @PostMapping("/like")
    @ApiOperation(value = "entityType:1-技术问答、2-评论")
    public ResultVO like(int entityType, Long entityId, Long entityUserId, Long techqaId) {
        User user = hostHolder.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 点赞状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件（系统通知） - 取消点赞不通知
        if (likeStatus == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", techqaId);
            eventProducer.fireEvent(event);
        }

        if (entityType == ENTITY_TYPE_TECHQA) {
            // 计算帖子分数
            String redisKey = RedisKeyUtil.getTechqaScoreKey();
            redisTemplate.opsForSet().add(redisKey, techqaId);
        }

        ResultVO resultVO = ResultVO.success("点赞成功");
        resultVO.data("data", map);
        return resultVO;
    }

}