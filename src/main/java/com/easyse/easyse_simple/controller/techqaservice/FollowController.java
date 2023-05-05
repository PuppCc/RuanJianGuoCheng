package com.easyse.easyse_simple.controller.techqaservice;

/**
 * 关注(目前只做了关注用户)
 */

import com.easyse.easyse_simple.constants.Constants;
import com.easyse.easyse_simple.event.EventProducer;
import com.easyse.easyse_simple.pojo.DO.Page;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.Event;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.FollowService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.easyse.easyse_simple.constants.KafkaConstant.TOPIC_FOLLOW;
import static com.easyse.easyse_simple.constants.TechqaConstant.ENTITY_TYPE_USER;

@RestController
@Api(tags = "关注相关")
public class FollowController implements Constants {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 关注
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping("/follow")
    public ResultVO follow(int entityType, Long entityId) {
        User user = hostHolder.getUser();

        followService.follow(user.getId(), entityType, entityId);

        // 触发关注事件（系统通知）
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);

        return ResultVO.success("关注成功");
    }

    /**
     * 取消关注
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping("/unfollow")
    @ApiOperation(value = "取消关注")
    public ResultVO unfollow(int entityType, Long entityId) {
        User user = hostHolder.getUser();

        followService.unfollow(user.getId(), entityType, entityId);

        return ResultVO.success("已取消关注");
    }

    /**
     * 某个用户的关注列表（人）
     *
     * @param userId
     * @param page
     * @return
     */
    @GetMapping("/followees/{userId}")
    @ApiOperation(value = "某个用户的关注列表（人）")
    public ResultVO getFollowees(@PathVariable("userId") Long userId, Page page) {
        ResultVO resultVO = ResultVO.success();
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        resultVO.data("user", user);

        page.setLimit(30);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        // 获取关注列表
        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());

        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user"); // 被关注的用户
                map.put("hasFollowed", hasFollowed(u.getId())); // 判断当前登录用户是否已关注这个关注列表中的某个用户
            }
        }
        resultVO.data("users", userList);

        return resultVO;
    }

    /**
     * 某个用户的粉丝列表
     *
     * @param userId
     * @param page
     * @return
     */
    @GetMapping("/followers/{userId}")
    public ResultVO getFollowers(@PathVariable("userId") Long userId, Page page) {

        ResultVO resultVO = ResultVO.success();
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        resultVO.data("user", user);

        page.setLimit(30);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));

        // 获取关注列表
        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());

        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user"); // 被关注的用户
                map.put("hasFollowed", hasFollowed(u.getId())); // 判断当前登录用户是否已关注这个关注列表中的某个用户
            }
        }

        resultVO.data("users", userList);

        return resultVO;
    }

    /**
     * 判断当前登录用户是否已关注某个用户
     *
     * @param userId 某个用户
     * @return
     */
    private boolean hasFollowed(Long userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }

        return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }

}