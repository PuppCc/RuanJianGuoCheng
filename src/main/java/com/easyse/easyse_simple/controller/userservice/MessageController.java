package com.easyse.easyse_simple.controller.userservice;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easyse.easyse_simple.constants.Constants;
import com.easyse.easyse_simple.pojo.DO.Page;
import com.easyse.easyse_simple.pojo.DO.person.Message;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.kafkaserivce.MessageService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

import static com.easyse.easyse_simple.constants.KafkaConstant.*;
import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @author: zky
 * @date: 2022/11/30
 * @description: 系统通知、消息相关
 */
@RestController
@Api(tags = "系统通知、消息相关")
public class MessageController implements Constants {

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * 私信列表
     * @param
     * @return
     */
    @GetMapping("/letter/list/{userId}")
    @ApiOperation(value = "私信列表")
    public ResultVO getLetterList(@PathVariable("userId")Long id) {
        // Integer.valueOf("abc"); // 测试统一异常处理（普通请求）
        // 分页信息
        Page page = new Page();
        page.setLimit(30);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCout(id));
        // 私信列表
        List<Message> conversationList = messageService.findConversations(
                id, page.getOffset(), page.getLimit());

        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                // 私信
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(
                        message.getConversationId())); // 私信数量
                map.put("unreadCount", messageService.findLetterUnreadCount(
                        id, message.getConversationId())); // 未读私信数量
                Long targetId = id.equals(message.getFromId()) ? message.getToId() : message.getFromId();
                map.put("target", userService.getById(targetId)); // 私信对方

                conversations.add(map);
            }
        }
        ResultVO resultVO = ResultVO.success();
        resultVO.data("conversations", conversations);

        // 查询当前用户的所有未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(id, null);
        resultVO.data("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnReadCount(id, null);
        resultVO.data("noticeUnreadCount", noticeUnreadCount);

        return resultVO;

    }

    /**
     * 私信详情页
     * @param conversationId
     * @param page
     * @param model
     * @return
     */
    @GetMapping("/letter/detail/{conversationId}")
    @ApiOperation(value = "私信详情：conversationId为私信唯一标识")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());

        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.getById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        // 私信目标
        model.addAttribute("target", getLetterTarget(conversationId));

        // 将私信列表中的未读消息改为已读
        List<Long> ids = getUnreadLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/letter-detail";
    }

    /**
     * 获取私信对方对象
     * @param conversationId
     * @return
     */
    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (userHolder.getUser().getId() == id0) {
            return userService.getById(id1);
        }
        else {
            return userService.getById(id0);
        }
    }

    /**
     * 获取当前登录用户未读私信的 id
     * @param letterList
     * @return
     */
    private List<Long> getUnreadLetterIds(List<Message> letterList) {
        List<Long> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                // 当前用户是私信的接收者且该私信处于未读状态
                if (userHolder.getUser().getId().equals(message.getToId()) && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

    /**
     * 获取当前登录用户未读私信的 id
     * @param letterList
     * @return
     */
    private List<Long> getUnreadLetterIds(Long id, List<Message> letterList) {
        List<Long> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                // 当前用户是私信的接收者且该私信处于未读状态
                if (id.equals(message.getToId()) && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

    /**
     * 发送私信
     * @param toName 收信人 username
     * @param content 内容
     * @return
     */
    @PostMapping("/letter/send")
    @ResponseBody
    @ApiOperation(value = "发送私信：接收者、内容")
    public ResultVO sendLetter(String toName, String content) {
        // Integer.valueOf("abc"); // 测试统一异常处理（异步请求）
        LambdaQueryWrapper<User> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(User::getUsername, toName);
        User target = userService.getOne(objectLambdaQueryWrapper);
        if (target == null) {
            return ResultVO.failure(USER_NOTFOUND);
        }

        Message message = new Message();
        message.setFromId(userHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        }
        else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setStatus(0); // 默认就是 0 未读，可不写

        messageService.addMessage(message);

        return ResultVO.success("");
    }

    /**
     * 通知列表（只显示最新一条消息）
     * @return
     */
    @GetMapping("/notice/list/{id}")
    @ApiOperation(value = "通知列表：废弃不用")
    public ResultVO getNoticeList(@PathVariable("id")Long id) {
        ResultVO resultVO = ResultVO.success("查询通知成功~");
        // 查询评论类通知
        Message message = messageService.findLatestNotice(id, TOPIC_COMMNET);
        // 封装通知需要的各种数据
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.getById(id));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(id, TOPIC_COMMNET);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(id, TOPIC_COMMNET);
            messageVO.put("unread", unread);

            resultVO.data("commentNotice", messageVO);
        }

        // 查询点赞类通知
        message = messageService.findLatestNotice(id, TOPIC_LIKE);
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.getById((Long) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(id, TOPIC_LIKE);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(id, TOPIC_LIKE);
            messageVO.put("unread", unread);

            resultVO.data("likeNotice", messageVO);
        }

        // 查询关注类通知
        message = messageService.findLatestNotice(id, TOPIC_FOLLOW);
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.getById((Long) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));

            int count = messageService.findNoticeCount(id, TOPIC_FOLLOW);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(id, TOPIC_FOLLOW);
            messageVO.put("unread", unread);

            resultVO.data("followNotice", messageVO);
        }

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(id, null);
        resultVO.data("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnReadCount(id, null);
        resultVO.data("noticeUnreadCount", noticeUnreadCount);

        return resultVO;
    }

    /**
     * 查询某个主题所包含的通知列表
     * @param topic
     * @return
     */
    @GetMapping("/notice/detail/{userId}/{topic}")
    @ApiOperation(value = "某个主题所包含的通知列表：comment、like、follow")
    public ResultVO getNoticeDetail(@PathVariable("topic")@ApiParam(value = "comment：评论、like：点赞、follow：关注") String topic,
                                    @PathVariable("userId")Long id) {

        ResultVO resultVO = ResultVO.success();
        Page page = new Page();
        page.setLimit(30);
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(id, topic));

        List<Message> noticeList = messageService.findNotices(id, topic,page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.getById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                // 发送系统通知的作者
                map.put("fromUser", userService.getById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        resultVO.data("notices", noticeVoList);

        // 设置已读
        List<Long> ids = getUnreadLetterIds(id, noticeList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return resultVO;
    }
}
