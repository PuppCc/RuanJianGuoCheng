package com.easyse.easyse_simple.controller.techqaservice;


import com.easyse.easyse_simple.event.EventProducer;
import com.easyse.easyse_simple.pojo.DO.Page;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.pojo.Event;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.ElasticsearchService;
import com.easyse.easyse_simple.service.techqaservice.LikeService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.easyse.easyse_simple.constants.KafkaConstant.TOPIC_SEARCH;
import static com.easyse.easyse_simple.constants.TechqaConstant.ENTITY_TYPE_TECHQA;
import static com.easyse.easyse_simple.utils.IntToLong.toLong;

/**
 * @author: zky
 * @date: 2022/11/30
 * @description: es搜索
 */
@RestController
@Api(tags = "搜索相关")
public class SearchController {


    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 搜索
     * search?keword=xxx
     * @param keyword 关键词
     * @param page
     * @return
     */
    @GetMapping("/search")
    @ApiOperation(value = "keyword：关键字，分页信息")
    public ResultVO search(String keyword, Page page) {
        // TODO 将用户的搜索关键词记录到日志，进行实时分析，并根据此来进行喜好推荐
        Long userId = Objects.isNull(userHolder.getUser()) ? toLong(1) : userHolder.getUser().getId();
        Event event = new Event().setTopic(TOPIC_SEARCH)
                .setUserId(userId)
                .setData("keyword", keyword);
        eventProducer.fireEvent(event);
        // 搜索帖子 (Spring 提供的 Page 当前页码从 0 开始计数)
        List<Techqa> techqas =
                elasticsearchService.searchTechqa(keyword, page.getCurrent()-1, page.getLimit());
        // 聚合数据
        List<Map<String, Object>> resTechqas = new ArrayList<>();
        if (techqas != null) {
            for (Techqa techqa : techqas) {
                Map<String, Object> map = new HashMap<>();
                // 帖子
                map.put("techqa", techqa);
                // 作者
                map.put("user", userService.getById(techqa.getUserId()));
                // 点赞数量
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_TECHQA, techqa.getId()));

                resTechqas.add(map);
            }
        }
        ResultVO resultVO = ResultVO.success("查询数据成功");
        resultVO.data("resTechqas", resTechqas);
        resultVO.data("keyword", keyword);

        // 设置分页
        page.setPath("/search?keyword="+ keyword);
        page.setRows(techqas == null ? 0 : techqas.size());

        return resultVO;
    }
}
