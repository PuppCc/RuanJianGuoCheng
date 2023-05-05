package com.easyse.easyse_simple.controller.techqaservice;


import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easyse.easyse_simple.annotations.RateLimit;
import com.easyse.easyse_simple.constants.Constants;
import com.easyse.easyse_simple.constants.TechqaConstant;
import com.easyse.easyse_simple.event.EventProducer;
import com.easyse.easyse_simple.pojo.DO.Page;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.DO.techqa.Comment;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.pojo.DO.techqa.TechqaTopic;
import com.easyse.easyse_simple.pojo.DO.techqa.Topic;
import com.easyse.easyse_simple.pojo.Event;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.ElasticsearchService;
import com.easyse.easyse_simple.service.techqaservice.*;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.LRU;
import com.easyse.easyse_simple.utils.RedisCache;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import com.easyse.easyse_simple.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static com.easyse.easyse_simple.constants.KafkaConstant.TOPIC_DELETE;
import static com.easyse.easyse_simple.constants.KafkaConstant.TOPIC_PUBLISH;
import static com.easyse.easyse_simple.utils.IntToLong.toLong;
import static com.easyse.easyse_simple.utils.RedisKeyUtil.MONTH_RANK;
import static com.easyse.easyse_simple.utils.RedisKeyUtil.WEEK_RANK;


/**
 * @author: zky
 * @date: 2022/10/23
 * @description:
 */
@RestController
@RequestMapping("/techqa")
@Api(tags = "技术问答相关")
public class TechqaController implements TechqaConstant, Constants {

    @Autowired
    RedisCache redisCache;

    @Autowired
    TechqaService techqaService;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserHolder userHolder;

    @Autowired
    TechqaTopicService techqaTopicService;

    @Autowired
    TopicService topicService;


    /**
     * 进入首页
     * @param page
     * @param orderMode 默认是 0（最新）
     * @return
     */
    @GetMapping("/totechqa")
    @ApiOperation("技术问答列表首页：orderMode-0:最新、1：最热、2：周榜、3：月榜")
    @RateLimit
    public ResultVO getIndexPage(Page page, @RequestParam(name = "orderMode", defaultValue = "0") Integer orderMode) {
        if(orderMode <= 1) {
            // 获取总页数
            page.setRows(techqaService.findTechqaRows(toLong(0)));
            page.setPath("/totechqa?orderMode=" + orderMode);
            // 分页查询
            List<Techqa> list = techqaService.findTechqas(toLong(0), page.getOffset(), page.getLimit(), orderMode);
            // 封装帖子和该帖子对应的用户信息
            List<Map<String, Object>> techqas = new ArrayList<>();
            if (list != null) {
                for (Techqa techqa : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("techqa", techqa);
                    User user = userService.getById(techqa.getUserId());
                    map.put("user", user);
                    long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_TECHQA, techqa.getId());
                    map.put("likeCount", likeCount);
                    String visitTechqa = RedisKeyUtil.getVisitTechqa(techqa.getId());
                    Object cacheObject = redisCache.getCacheObject(visitTechqa);
                    Integer visitcount;
                    visitcount = Objects.isNull(cacheObject) ? 0 : (Integer)cacheObject;
                    map.put("visitcount", visitcount);
                    List<Long> topicIds = techqaTopicService.getTopicIds(techqa.getId());
                    List<Topic> topics = new ArrayList<>();
                    topicIds.forEach(aLong -> {
                        Topic byId = topicService.getById(aLong);
                        topics.add(byId);
                    });
                    map.put("topics", topics);
                    techqas.add(map);
                }
            }
            ResultVO resultVO = ResultVO.success("技术问答首页查询成功");
            resultVO.data("techqas", techqas);
            resultVO.data("page", page);
            resultVO.data("orderMode", orderMode);
            return resultVO;
        } else {
            // TODO 月榜、周榜查询
            List<Map<String, Object>> techqas = new ArrayList<>();
            //当前日期所在周的第一天
            if(orderMode == 2) {
                Calendar calendar = CalendarUtil.calendar();
                LocalDateTime weekBeginDay = CalendarUtil.toLocalDateTime(CalendarUtil.beginOfWeek(calendar));
                String weekBeginDayStr = LocalDateTimeUtil.format(weekBeginDay, "yyyy-MM-dd");
                String weekRankKey = WEEK_RANK + weekBeginDayStr; //周榜缓存key yyyy-MM-dd:weekRank
                Set<ZSetOperations.TypedTuple<Object>> set = redisCache.zReverseRangeWithScore(weekRankKey);
                set.forEach(objectTypedTuple -> {
                    Long techqaId = (Long)objectTypedTuple.getValue();
                    Techqa techqa = techqaService.getById(techqaId);
                    User user = userService.getById(techqa.getUserId());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("techqa", techqa);
                    map.put("user", user);

                    long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_TECHQA, techqa.getId());
                    map.put("likeCount", likeCount);
                    String visitTechqa = RedisKeyUtil.getVisitTechqa(techqa.getId());
                    Object cacheObject = redisCache.getCacheObject(visitTechqa);
                    Integer visitcount;
                    visitcount = Objects.isNull(cacheObject) ? 0 : (Integer)cacheObject;
                    map.put("visitcount", visitcount);
                    List<Long> topicIds = techqaTopicService.getTopicIds(techqa.getId());
                    List<Topic> topics = new ArrayList<>();
                    topicIds.forEach(aLong -> {
                        Topic byId = topicService.getById(aLong);
                        topics.add(byId);
                    });
                    map.put("topics", topics);
                    techqas.add(map);
                });
            }

            if(orderMode == 3) {
                //当前日期所在月的第一天
                LocalDateTime monthBeginDay = CalendarUtil.toLocalDateTime(CalendarUtil.beginOfMonth(CalendarUtil.calendar()));
                String monthBeginDayStr = LocalDateTimeUtil.format(monthBeginDay, "yyyy-MM-dd");
                String monthRankKey = MONTH_RANK + monthBeginDayStr; //月榜缓存key yyyy-MM-dd:monthRank
                Set<ZSetOperations.TypedTuple<Object>> set = redisCache.zReverseRangeWithScore(monthRankKey);
                set.forEach(objectTypedTuple -> {
                    Long techqaId = (Long)objectTypedTuple.getValue();
                    Techqa techqa = techqaService.getById(techqaId);
                    User user = userService.getById(techqa.getUserId());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("techqa", techqa);
                    map.put("user", user);

                    long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_TECHQA, techqa.getId());
                    map.put("likeCount", likeCount);
                    String visitTechqa = RedisKeyUtil.getVisitTechqa(techqa.getId());
                    Object cacheObject = redisCache.getCacheObject(visitTechqa);
                    Integer visitcount;
                    visitcount = Objects.isNull(cacheObject) ? 0 : (Integer)cacheObject;
                    map.put("visitcount", visitcount);
                    List<Long> topicIds = techqaTopicService.getTopicIds(techqa.getId());
                    List<Topic> topics = new ArrayList<>();
                    topicIds.forEach(aLong -> {
                        Topic byId = topicService.getById(aLong);
                        topics.add(byId);
                    });
                    map.put("topics", topics);
                    techqas.add(map);
                });
            }

            ResultVO resultVO = ResultVO.success("技术问答首页查询成功");
            resultVO.data("techqas", techqas);
            return resultVO;
        }


    }

    /**
     * 添加技术问答
     * @param techqa
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加技术问答：title、content非空"/*，需要先请求防重令牌*/)
//    @ApiIdempotent
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ResultVO addTechqa(@RequestBody @Validated Techqa techqa,
                              @ApiParam(value = "标签组id") Long[] topicIds) {
        // 因为需要过滤敏感词，不直接使用Mybatis-Plus
        techqa.setUserId(userHolder.getUser().getId());
        techqaService.addTechqa(techqa);
        // 关联标签
        if(!Objects.isNull(topicIds)) {
            Arrays.stream(topicIds).forEach(aLong -> techqaTopicService.save(new TechqaTopic(techqa.getId(), aLong)));
        }
        // TODO 触发事件、通过消息队列存入ES服务器
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(techqa.getUserId())
                .setEntityType(ENTITY_TYPE_TECHQA)
                .setEntityId(techqa.getId());
        eventProducer.fireEvent(event);
        // TODO 计算分数
        String redisKey = RedisKeyUtil.getTechqaScoreKey();
        redisTemplate.opsForSet().add(redisKey, techqa.getId());


        return ResultVO.success("发布技术问答成功！");
    }

    /**
     * 删除技术问答
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据ID删除技术问答：id为技术问答的id")
    public ResultVO deleteTechqa(@PathVariable("id")Long id, Long userid) {
        techqaService.updateStatus(id, TECHQA_DELETED);

        // TODO 触发删帖事件，通过消息队列更新 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(TOPIC_DELETE)
                .setUserId(userid)
                .setEntityType(ENTITY_TYPE_TECHQA)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        QueryWrapper<TechqaTopic> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("techqa_id", id);
        if(techqaTopicService.remove(queryWrapper)){
            return ResultVO.failure(TOPIC_TECHQA_DELETE_SUCCESS);
        }
        return ResultVO.success("删除成功！");
    }

    /**
     * 置顶技术问答
     * @param id
     * @return
     */
    @PostMapping("/top")
    @ResponseBody
    @ApiOperation(value = "置顶加精技术问答", notes = "0-普通; 1-置顶")
    public ResultVO updateTop(Long id, int type, Long userid) {
        techqaService.updateType(id, type);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(userid)
                .setEntityType(ENTITY_TYPE_TECHQA)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return ResultVO.success("置顶成功");
    }


    /**
     * 加精技术问答
     * @param id
     * @return
     */
    @PostMapping("/wonderful")
    @ResponseBody
    @ApiOperation(value = "加精技术问答", notes = "0-正常; 1-精华; 2-拉黑")
    public ResultVO setWonderful(Long id, Long userid) {
        techqaService.updateStatus(id, 1);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(userid)
                .setEntityType(ENTITY_TYPE_TECHQA)
                .setEntityId(userid);
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getTechqaScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        return ResultVO.success("加精成功");
    }



    /**
     * 技术问答详情页
     * @param techqaId
     * @return
     */
    @GetMapping("/detail/{techqaId}")
    @ApiOperation(value = "技术问答详情页：techqaId为对应技术问答的id")
    public ResultVO getTechqa(@PathVariable("techqaId") Long techqaId, Page page) {
        ResultVO resultVO = ResultVO.success("查询详情页成功");
        // 技术问答
        Techqa techqa = techqaService.getById(techqaId);
        // 内容反转义，不然 markDown 格式无法显示
        String content = HtmlUtils.htmlUnescape(techqa.getContent());
        techqa.setContent(content);
        resultVO.data("techqa", techqa);
        User user = userService.getById(techqa.getUserId());
        resultVO.data("user", user);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_TECHQA, techqaId);
        resultVO.data("likecount", likeCount);
        // 当前登录用户的点赞状态
        int likeStatus = userHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(userHolder.getUser().getId(), ENTITY_TYPE_TECHQA, techqaId);
        resultVO.data("likestatus", likeStatus);

        // 评论分页信息
        page.setLimit(5);
        page.setPath("/techqa/detail/" + techqaId);
        page.setRows(techqa.getCommentAmount());

        // 帖子的评论列表
        List<Comment> commentList = commentService.findCommentByEntity(
                ENTITY_TYPE_TECHQA, techqa.getId(), page.getOffset(), page.getLimit());

        // 封装评论及其相关信息
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 存储对帖子的评论
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 发布评论的作者
                commentVo.put("user", userService.getById(comment.getUserId()));
                // 该评论点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                // 当前登录用户对该评论的点赞状态
                likeStatus = userHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(
                        userHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);


                // 存储每个评论对应的回复（不做分页）
                List<Comment> replyList = commentService.findCommentByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 封装对评论的评论和评论的作者信息
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 发布该回复的作者
                        replyVo.put("user", userService.getById(reply.getUserId()));
                        User target = reply.getTargetId() == 0 ? null : userService.getById(techqa.getUserId());
                        // 该回复的目标用户
                        replyVo.put("target", target);
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        // 该回复的点赞数量
                        replyVo.put("likeCount", likeCount);
                        likeStatus = userHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(
                                userHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        // 当前登录用户的点赞状态
                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 每个评论对应的回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        // 标签
        List<Long> topicIds = techqaTopicService.getTopicIds(techqa.getId());
        List<Topic> topics = new ArrayList<>();
        topicIds.forEach(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                Topic byId = topicService.getById(aLong);
                topics.add(byId);
            }
        });
        resultVO.data("topics", topics);

        resultVO.data("comments", commentVoList);

        return resultVO;

    }

    /**
     * 增加有效访问次数
     * @param techqaid
     * @return
     */
    @GetMapping("/visit/{techqaid}")
    @ApiOperation(value = "当满足条件时，为技术问答访问次数加1")
    public ResultVO visitadd(@PathVariable("techqaid")Long techqaid){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String visitTechqa = RedisKeyUtil.getVisitTechqa(techqaid);
                // 开启事务
                Object cacheObject = redisCache.getCacheObject(visitTechqa);
                redisOperations.multi();
                Integer visitcount;
                if (Objects.isNull(cacheObject)) {
                    redisCache.setCacheObject(visitTechqa, 1);
                } else {
                    visitcount = (Integer)cacheObject;
                    redisCache.setCacheObject(visitTechqa, ++visitcount);
                }
                // 提交事务
                return redisOperations.exec();
            }
        });
        return ResultVO.success("访问次数添加成功");
    }

    /**
     * 推荐技术问答
     */
    @GetMapping("{id}")
    @ApiOperation(value = "技术问答推荐")
    @RateLimit
    public ResultVO getHobby(@PathVariable("id") Long id) {
        LRU hobby = userHolder.getHobby(id);
        // 如果为空，则展示最新的
        if(Objects.isNull(hobby)){
            // 获取总页数
            Page page = new Page();
            page.setRows(techqaService.findTechqaRows(toLong(0)));
            // 分页查询
            List<Techqa> list = techqaService.findTechqas(toLong(0), page.getOffset(), page.getLimit(), 0);
            // 封装技术问答和该技术对应的用户信息
            List<Map<String, Object>> techqas = new ArrayList<>();
            if (list != null) {
                for (Techqa techqa : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("techqa", techqa);
                    User user = userService.getById(techqa.getUserId());
                    map.put("user", user);
                    long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_TECHQA, techqa.getId());
                    map.put("likeCount", likeCount);
                    String visitTechqa = RedisKeyUtil.getVisitTechqa(techqa.getId());
                    Object cacheObject = redisCache.getCacheObject(visitTechqa);
                    Integer visitcount;
                    visitcount = Objects.isNull(cacheObject) ? 0 : (Integer)cacheObject;
                    map.put("visitcount", visitcount);
                    List<Long> topicIds = techqaTopicService.getTopicIds(techqa.getId());
                    List<Topic> topics = new ArrayList<>();
                    topicIds.forEach(aLong -> {
                        Topic byId = topicService.getById(aLong);
                        topics.add(byId);
                    });
                    map.put("topics", topics);
                    techqas.add(map);
                }
            }
            ResultVO resultVO = ResultVO.success("系统没有您的信息，已为您推荐最新技术问答");
            resultVO.data("techqas", techqas);
            resultVO.data("page", page);
            return resultVO;
        }

        List<String> hobbies = hobby.getHobby();
        // 封装推荐的技术问答和该技术对应的用户信息
        List<Map<String, Object>> techqas = new ArrayList<>();
        hobbies.forEach(s -> {
            List<Techqa> techqas1 = elasticsearchService.searchTechqa(s, 0, 10);
            // 存储第一关匹配的
            Techqa techqa = techqas1.get(0);

            Map<String, Object> map = new HashMap<>();
            map.put("techqa", techqa);
            User user = userService.getById(techqa.getUserId());
            map.put("user", user);
            long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_TECHQA, techqa.getId());
            map.put("likeCount", likeCount);
            String visitTechqa = RedisKeyUtil.getVisitTechqa(techqa.getId());
            Object cacheObject = redisCache.getCacheObject(visitTechqa);
            Integer visitcount;
            visitcount = Objects.isNull(cacheObject) ? 0 : (Integer)cacheObject;
            map.put("visitcount", visitcount);
            List<Long> topicIds = techqaTopicService.getTopicIds(techqa.getId());
            List<Topic> topics = new ArrayList<>();
            topicIds.forEach(aLong -> {
                Topic byId = topicService.getById(aLong);
                topics.add(byId);
            });
            map.put("topics", topics);
            techqas.add(map);
        });
        ResultVO resultVO = ResultVO.success("技术问答推荐查询成功");
        resultVO.data("techqas", techqas);
        return resultVO;
    }
}
