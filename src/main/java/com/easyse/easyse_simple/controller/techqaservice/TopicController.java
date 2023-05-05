package com.easyse.easyse_simple.controller.techqaservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easyse.easyse_simple.annotations.Log;
import com.easyse.easyse_simple.annotations.RateLimit;
import com.easyse.easyse_simple.constants.Constants;
import com.easyse.easyse_simple.enums.BusinessType;
import com.easyse.easyse_simple.pojo.DO.Page;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.DO.techqa.TechqaTopic;
import com.easyse.easyse_simple.pojo.DO.techqa.Topic;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.techqaservice.TechqaTopicService;
import com.easyse.easyse_simple.service.techqaservice.TopicService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.easyse.easyse_simple.utils.IntToLong.toLong;


/**
 * @author: zky
 * @date: 2022/11/26
 * @description:
 */
@RestController
@RequestMapping("/techqa/topic")
@Api(tags = "标签相关")
public class TopicController implements Constants {

    @Autowired
    UserService userService;

    @Autowired
    TopicService topicService;

    @Autowired
    TechqaTopicService techqaTopicService;

    /**
     * 添加标签
     * @param topic topic信息
     * @return
     */
    @PostMapping("/add")
    @Log(title = "新增topic", businessType = BusinessType.IMPORT)
    @ApiOperation(value = "新增类型标签：userid、description不能为空")
    public ResultVO addTopic(@RequestBody Topic topic){
        Long useridId = topic.getUserId();
        // 找到创建者，赋值createBy
        User createUser = userService.getById(useridId);
        if(Objects.isNull(createUser)) {
            return ResultVO.failure(USER_NOTFOUND);
        }
        topic.setCreateBy(createUser.getUsername());
        topicService.save(topic);
        return ResultVO.success(TOPIC_ADD_SUCCESS);
    }

    /**
     * 删除标签
     * @param topicid 标签id
     * @return
     */
    @DeleteMapping("/{topicid}")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @ApiOperation(value = "通过标签id删除标签")
    public ResultVO deleteTopic(@PathVariable("topicid") Long topicid){
        if(topicService.removeById(topicid)) {
            return ResultVO.failure(TOPIC_DELETE_FIAL);
        }
        //TODO 删除techqa_topicqa表
        QueryWrapper<TechqaTopic> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("topic_id", topicid);
        techqaTopicService.remove(queryWrapper);
        return ResultVO.success(TOPIC_DELETE_SUCCESS);
    }

    /**
     * 修改标签
     * @param topic 标签信息
     * @return
     */
    @PutMapping("/{topicid}")
    @Log(title = "删除", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "更新标签：topicid为要更新标签的id，topic为内容（userid、description不能为空）")
    public ResultVO updateTopic(@PathVariable("topicid") Long topicid,
                                @RequestBody Topic topic){
        if(!topicService.updateById(topic)){
            ResultVO.success(TOPIC_UPDATE_FAIL);
        }
        topicService.updateById(topic);
        return ResultVO.success(TOPIC_UPDATE_SUCCESS);
    }

    /**
     * 为id为techqaid的技术问答打上对应id为topicid标签
     * @param techqaid
     * @param topicid
     * @return
     */
    @DeleteMapping ("/{techqaid}/{topicid}")
    @Log(title = "新增", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "标签技术问答关联表：（暂时不用）")
    public ResultVO deleteTopictoTechqa(@PathVariable("techqaid")Long techqaid,
                             @PathVariable("topicid")Long topicid){
        if(techqaTopicService.save(new TechqaTopic(techqaid, topicid))){
            return ResultVO.failure(TOPIC_TECHQA_ADD_FAIL);
        }
        Topic topic = topicService.getById(topicid);
        topic.setTechqaAmount(topic.getTechqaAmount() + 1);
        topicService.updateById(topic);
        return ResultVO.success(TOPIC_TECHQA_ADD_SUCCESS);
    }

    /**
     * 为id为techqaid的技术问答取消对应id为topicid标签
     * @param techqaid
     * @return
     */
    @PutMapping("/{techqaid}/{topicid}")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @ApiOperation(value = "标签技术问答关联表：（暂时不用）")
    public ResultVO addTopictoTechqa(@PathVariable("techqaid")Long techqaid,
                                     @PathVariable("topicid")Long topicid){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.gt("techqa_id", techqaid);
        queryWrapper.gt("topic_id", topicid);
        if(!techqaTopicService.remove(queryWrapper)){
            return ResultVO.failure(TOPIC_TECHQA_DELETE_FAIL);
        }
        Topic topic = topicService.getById(topicid);
        topic.setTechqaAmount(topic.getTechqaAmount() - 1);
        topicService.updateById(topic);
        return ResultVO.success(TOPIC_TECHQA_ADD_SUCCESS);
    }

    /**
     * 标签的热度，根据被文章的引用数计算
     * @param orderMode
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "热门标签：ordermodel=0、1-最新、最热")
    @RateLimit
    public ResultVO topicList(@RequestParam(name = "orderMode", defaultValue = "0") Integer orderMode){
        // 获取总页数
        Page page = new Page();
        page.setRows(topicService.getTopicRows());
        page.setPath("/index?orderMode=" + orderMode);
        page.setLimit(10);

        List<Topic> topics = topicService.getTopics(toLong(0), page.getOffset(), page.getLimit(), orderMode);
        // 封装标签和标签对应的用户信息
        List<Map<String, Object>> techqas = new ArrayList<>();
        if (topics != null) {
            for (Topic topic : topics) {
                Map<String, Object> map = new HashMap<>();
                map.put("topic", topic);
                User user = userService.getById(topic.getUserId());
                if(Objects.isNull(user)) {
                    user = User.builder().username("admin").build();
                }
                map.put("username", user.getUsername());
                techqas.add(map);
            }
        }
        ResultVO resultVO = ResultVO.success("标签查询成功");
        resultVO.data("topics", topics);
        return resultVO;
    }
}
