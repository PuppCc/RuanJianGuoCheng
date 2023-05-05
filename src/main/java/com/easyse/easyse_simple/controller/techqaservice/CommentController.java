package com.easyse.easyse_simple.controller.techqaservice;

import com.easyse.easyse_simple.annotations.RateLimit;
import com.easyse.easyse_simple.event.EventProducer;
import com.easyse.easyse_simple.pojo.DO.Page;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.DO.techqa.Comment;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.pojo.Event;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.techqaservice.CommentService;
import com.easyse.easyse_simple.service.techqaservice.TechqaService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import com.easyse.easyse_simple.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.easyse.easyse_simple.constants.KafkaConstant.TOPIC_COMMNET;
import static com.easyse.easyse_simple.constants.KafkaConstant.TOPIC_PUBLISH;
import static com.easyse.easyse_simple.constants.TechqaConstant.ENTITY_TYPE_COMMENT;
import static com.easyse.easyse_simple.constants.TechqaConstant.ENTITY_TYPE_TECHQA;
import static com.easyse.easyse_simple.utils.IntToLong.toLong;

/**
 * @author: zky
 * @date: 2022/11/20
 * @description: 评论相关
 */
@RestController
@RequestMapping("/techqa/comment")
@Api(tags = "评论相关")
public class CommentController {

    @Autowired
    private UserHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TechqaService techqaService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 添加评论
     * @param techqaId
     * @param comment
     * @return
     */
    @PostMapping("/add/{techqaId}/{userId}")
    @ApiOperation(value = "添加评论：techqaId为对应技术问答的id,userId为评论的用户Id,entityType（1-技术问答；2-评论）")
    public ResultVO addComment(@PathVariable("techqaId") Long techqaId,
                               @PathVariable("userId") Long userId,
                               @Validated @RequestBody Comment comment) {
        comment.setUserId(userId);
        comment.setIsDeleted(0);
        commentService.addComment(comment);

        // 触发评论事件（系统通知）
        Event event = new Event()
                .setTopic(TOPIC_COMMNET)
                .setUserId(userId)
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("techId", techqaId);
        if (comment.getEntityType() == ENTITY_TYPE_TECHQA) {
            Techqa target = techqaService.getById(comment.getEntityId());
            if(Objects.isNull(target)) {
                return ResultVO.failure("评论的技术问答Id为空，请修改entityId");
            }
            event.setEntityUserId(target.getUserId());
            // 计算帖子评论数
            String redisKey = RedisKeyUtil.getTechqaCommentKey();
            redisTemplate.opsForSet().add(redisKey, techqaId);
        }
        else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.getById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        if (comment.getEntityType() == ENTITY_TYPE_TECHQA) {
            // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_TECHQA)
                    .setEntityId(techqaId);
            eventProducer.fireEvent(event);

            // 计算帖子分数
            String redisKey = RedisKeyUtil.getTechqaScoreKey();
            redisTemplate.opsForSet().add(redisKey, techqaId);
        }

        return ResultVO.success("评论成功");
    }

    /**
     * 删除评论
     * @param techqaId
     * @return
     */
    @DeleteMapping("/{techqaId}")
    @ApiOperation(value = "删除评论：techqaId为对应技术问答的id")
    public ResultVO deleteComment(@PathVariable("techqaId") Long techqaId) {
        int res = commentService.deleteComment(techqaId);
        return res != 0 ? ResultVO.success("删除评论成功") : ResultVO.failure("删除评论失败");
    }

    /**
     * 评论的热度，根据被点赞的次数
     * @param orderMode
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "热门评论：ordermodel=0、1-最新、最热")
    @RateLimit
    public ResultVO commentList(@RequestParam(name = "orderMode", defaultValue = "0") Integer orderMode){
        // 获取总页数
        Page page = new Page();
        page.setRows(commentService.findCommentRows());
        page.setPath("/index?orderMode=" + orderMode);
        page.setLimit(10);

        // 分页查询
        List<Comment> list = commentService.findComments(toLong(0), page.getOffset(), page.getLimit(), orderMode);
        // 封装帖子和该帖子对应的用户信息
        List<Map<String, Object>> comments = new ArrayList<>();
        if (list != null) {
            for (Comment comment : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);
                User user = userService.getById(comment.getUserId());
                map.put("user", user);
                comments.add(map);
            }
        }
        ResultVO resultVO = ResultVO.success("评论列表查询成功");
        resultVO.data("comments", comments);
        return resultVO;
    }

}
