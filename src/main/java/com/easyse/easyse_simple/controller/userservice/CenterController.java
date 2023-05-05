package com.easyse.easyse_simple.controller.userservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyse.easyse_simple.pojo.DO.example.DesignModeCase;
import com.easyse.easyse_simple.pojo.DO.person.Colloction;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.DO.techqa.Comment;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.pojo.DO.techqa.Topic;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.exampleservice.DesignModeCaseService;
import com.easyse.easyse_simple.service.exampleservice.DesignModeService;
import com.easyse.easyse_simple.service.techqaservice.*;
import com.easyse.easyse_simple.service.userserivce.ColloctionService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.RedisCache;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import com.easyse.easyse_simple.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;

import static com.easyse.easyse_simple.constants.TechqaConstant.ENTITY_TYPE_TECHQA;

/**
 * @author: zky
 * @date: 2022/12/24
 * @description:
 */
@RestController
@RequestMapping(value = "/person")
@Api(tags = "个人中心相关")
public class CenterController {

    @Autowired
    UserService userService;

    @Autowired
    ColloctionService colloctionService;

    @Autowired
    TechqaService techqaService;

    @Autowired
    DesignModeCaseService designModeCaseService;

    @Autowired
    UserHolder userHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    RedisCache redisCache;

    @Autowired
    TechqaTopicService techqaTopicService;

    @Autowired
    TopicService topicService;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询用户")
    public User finduser(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "新增案例")
    public ResultVO addColloction(@RequestBody Colloction colloction){
        if(colloctionService.save(colloction)) {
            return ResultVO.success("收藏成功！");
        } else {
            return ResultVO.failure("收藏失败！");
        }
    }

    @GetMapping("/colloction/{userId}/{mode}")
    @ApiOperation(value = "我收藏的案例")
    public ResultVO list(@ApiParam(value = "0:技术问答、1：设计模式案例、2：场景设计")
                             @PathVariable(value = "mode") Integer mode,
                             @PathVariable(value = "userId")Long userId){
        ResultVO resultVO = ResultVO.success("查询收藏案例成功");
        LambdaQueryWrapper<Colloction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Colloction::getCollectType, mode);
        wrapper.eq(Colloction::getUserId, userId);
        List<Colloction> list = colloctionService.list(wrapper);
        List<HashMap<String, Object>> datas =  new ArrayList<>();
        if(mode == 0) {
            // 技术问答
            list.forEach(colloction -> {
                HashMap<String, Object> map = new HashMap<>();
                Long targetId = colloction.getTargetId();
                Techqa techqa = techqaService.getById(targetId);
                if(Objects.isNull(techqa)) {
                    techqa = Techqa.builder().build();
                }
                map.put("techqa", techqa);
                Long id = techqa.getUserId();
                User user = userService.getById(id);
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
                datas.add(map);
            });

        } else {
            // 设计模式案例
            list.forEach(colloction -> {
                HashMap<String, Object> map = new HashMap<>();
                Long targetId = colloction.getTargetId();
                DesignModeCase designModeCase = designModeCaseService.getById(targetId);
                if(Objects.isNull(designModeCase)) {
                    designModeCase = DesignModeCase.builder().build();
                }
                map.put("designModeCase", designModeCase);
                Long id = designModeCase.getUserId();
                User user = userService.getById(id);
                map.put("user", user);
                datas.add(map);
            });
        }

        // 技术问答
        resultVO.data("datas", datas);
        return resultVO;
    }

    @GetMapping("/mytechqa/{userid}")
    @ApiOperation(value = "我发布的技术问答")
    public ResultVO myTechqa(@PathVariable(value = "userid")Long userId){
        LambdaQueryWrapper<Techqa> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Techqa::getUserId, userId);
        List<Techqa> techqas = techqaService.list(wrapper);
        List<HashMap<String, Object>> datas =  new ArrayList<>();
        // 技术问答
        techqas.forEach(techqa -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("techqa", techqa);
            Long id = techqa.getUserId();
            User user = userService.getById(id);
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
            datas.add(map);
        });
        return ResultVO.success("我发布的技术问答查询成功").data("techqas", datas);
    }

    @GetMapping("/mytechqareply/{userid}")
    @ApiOperation(value = "我回答的技术问答")
    public ResultVO myTechqaRply(@PathVariable(value = "userid")Long userId){
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, userId);
        wrapper.eq(Comment::getEntityType, 1);
        List<Comment> comments = commentService.list(wrapper);
        List<HashMap<String , Object>> datas = new ArrayList<>();
        comments.forEach(comment -> {
            Long targetId = comment.getTargetId();
            Techqa techqa = techqaService.getById(targetId);
            if(Objects.isNull(techqa)) {
                techqa = Techqa.builder().build();
            }
            User user = userService.getById(techqa.getUserId());
            HashMap<String, Object> map = new HashMap<>();
            map.put("comment", comment);
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
            datas.add(map);
        });
        return ResultVO.success("我回答的技术问答查询成功").data("datas", datas);
    }



}
