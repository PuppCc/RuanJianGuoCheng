package com.easyse.easyse_simple.service.tasksservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easyse.easyse_simple.mapper.tasksservice.ClassMapper;
import com.easyse.easyse_simple.mapper.tasksservice.GroupMapper;
import com.easyse.easyse_simple.mapper.tasksservice.TaskMapper;
import com.easyse.easyse_simple.mapper.tasksservice.TtaskMapper;
import com.easyse.easyse_simple.mapper.userservice.UserMapper;
import com.easyse.easyse_simple.pojo.DO.task.*;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.tasksservice.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author ：rc
 * @date ：Created in 2022/10/17 23:43
 * @description：
 */

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TtaskMapper ttaskMapper;
    @Autowired
    ClassMapper classMapper;

    //验证码生成

    public static String getcode(){
        String code = "";
        char[] ch = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K','L', 'M', 'N', 'O', 'P', 'Q','R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','0','1','2','3','4','5','6','7','8','9'};
        for (int i = 0; i < 6; i++) {
            int index = (int)(Math.random() * ch.length);
            code = code + ch[index];
        }
        return code;
    }

    @Override
    public HashMap<String, Object> getGroupMembers(Long id) {

        //获取组长
        Group group = groupMapper.selectById(id);
        Long leaderId = group.getLeaderId();
        //
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("id,username").eq("group_id",id);
        List<User> users = userMapper.selectList(userQueryWrapper);
        HashMap<String,Object> map = new HashMap<>();
        map.put("users",users);
        map.put("leader",leaderId);
        //年级班级
        map.put("grade",group.getGradeId());
        Clazz clazz = classMapper.selectById(group.getClassId());
        map.put("BanJi",clazz.getClassName());
        return map;
    }

    @Override
    public ResultVO deleteGroup(Long id) {
        Group group = groupMapper.selectById(id);
        group.setIsDeleted(1);
        int i = groupMapper.updateById(group);
        if(1==i){
            return ResultVO.success("删除成功");
        }
        return ResultVO.failure("删除失败!");
    }

    @Override
    public ResultVO joinGroup(String teamCode, Long userID) {
        ResultVO resultVO;
        //查询小组代码是哪个小组 得到小组ID
        Map<String,Object> param = new HashMap<>();
        param.put("Invitationcode",teamCode);
        List<Group> groups = groupMapper.selectByMap(param);
        //修改学生信息 小组ID
        //检查小组是否满员
        if(groups.get(0).getNumber() >= groups.get(0).getMaxNumber()){
            resultVO = ResultVO.failure("加入失败，该小组已满员！!");
            return resultVO;
        }
        if(groups.isEmpty()){
            resultVO = ResultVO.failure("邀请码有误!");
            return resultVO;
        }else{
            User user = userMapper.selectById(userID);
            if(user.getGroupId() != 0){
                resultVO = ResultVO.failure("您已经有小组了！请先退出您所在的小组！");
                return  resultVO;
            }
            user.setGroupId(Long.valueOf(groups.get(0).getId()));
            int i = userMapper.updateById(user);
            if(i==1){
                //更新小组人数
                groups.get(0).setNumber(groups.get(0).getNumber()+1);
                groupMapper.updateById(groups.get(0));
                resultVO = ResultVO.success("加入小组成功！");
            }else {
                resultVO = ResultVO.failure("发生未知错误，加入小组失败！");
            }
        }
        return resultVO;
    }

    @Override
    public ResultVO quitGroup(Long userID) {
        ResultVO resultVO;
        User user = userMapper.selectById(userID);
        //要退出小组 只需要设置userID为0
        user.setGroupId(Long.valueOf(0));
        int i = userMapper.updateById(user);
        if(i==1){
                resultVO = ResultVO.success("成功退出小组！");
            }else {
                resultVO = ResultVO.failure("发生未知错误，退出小组失败！");
            }

        return resultVO;
    }

    @Override
    public ResultVO getGroupAllList() {
        ResultVO resultVO;
        HashMap<String, Object> map = new HashMap<>();
        map.put("is_deleted",0);
        List<Group> groups = groupMapper.selectByMap(map);

        ArrayList<Object> groupsList = new ArrayList<>();
        if(groups.isEmpty()){
            resultVO = ResultVO.failure("小组！");
        }else{
            resultVO = ResultVO.success("获取小组列表成功!");


            for(Group group : groups){
                //查询出该小组的上次tTask的 upload=1
                //上次是否上传
                HashMap<String,Object> resGroupInfo = new HashMap<>();
                Long id = group.getId();
                QueryWrapper<Ttask> queryWrapper = new QueryWrapper<>();
                //限制1条
                queryWrapper.select("upload").eq("group_id",id).last("limit 1");
                Ttask ttask = ttaskMapper.selectOne(queryWrapper);
                String upload = "0";
                if(!ObjectUtils.isEmpty(ttask)){
                    upload = ttask.getUpload();
                }

                resGroupInfo.put("group",group);
                resGroupInfo.put("upload",upload);
                //作业未完成次数
                queryWrapper.select("upload").eq("group_id",id).eq("upload",0);
                Long num = ttaskMapper.selectCount(queryWrapper);
                resGroupInfo.put("num",num);
                groupsList.add(resGroupInfo);
            }

            HashMap<String,Object> resMap=  new HashMap<>();
            resMap.put("groups",groupsList);
            resultVO.setData(resMap);
        }
        return resultVO;
    }


    @Override
    public ResultVO updateGroupInfo(Group group) {
        ResultVO resultVO;
        //更新小组信息，即项目信息
        int i = groupMapper.updateById(group);
        if(i==1){
            resultVO = ResultVO.success("小组项目信息更新成功！");
        }else{
            resultVO = ResultVO.failure("发生未知错误，小组项目信息更新失败！");
        }
        return resultVO;
    }

    @Override
    public ResultVO getProgress(Long groupID) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Task> qw = queryWrapper.select("group_id")
                .eq("group_id", groupID);
        if(ObjectUtils.isEmpty(taskMapper)){
            System.out.println("对象为空!");
        }
        Long sumNum = taskMapper.selectCount(qw);
        QueryWrapper<Task> queryWrapper2 = new QueryWrapper<>();
        QueryWrapper<Task> qw2 = queryWrapper2.select("group_id")
                .eq("group_id", groupID)
                .eq("finishedBy",null);
        Long unFinishedNum = taskMapper.selectCount(qw2);

        ResultVO resultVO = ResultVO.success("获取任务进度成功！");
        resultVO.data("finishedNum",sumNum - unFinishedNum);
        resultVO.data("unFinishedNum",unFinishedNum);
        return(resultVO);
    }

    @Override
    public Integer addGroup(Group group) {

        Long leaderId = group.getLeaderId();
        User user = userMapper.selectById(leaderId);
        if(ObjectUtils.isEmpty(user)){
            return 0;
        }else{
            //查小组长名字 防止重复创建
            String name = user.getUsername();
            Long userId = user.getId();
            QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("leader").eq("leader",name);
            Group tGroup = groupMapper.selectOne(queryWrapper);
            if(ObjectUtils.isEmpty(tGroup)){
                //没查到小组 说明这个学生没有成为组长
                //TODO查用户的group_id 防止已经加入小组的学生来创建小组
                QueryWrapper<User> userWrapper = new QueryWrapper<>();
                userWrapper.select("group_id").eq("id",userId);
                User tUser = userMapper.selectOne(userWrapper);
                if(0 == tUser.getGroupId()){
                    //这个学生没有小组 可以创建小组
                    //TODO 生成一个邀请码
                    String code = GroupServiceImpl.getcode();
                    //防止验证码重复
                    queryWrapper.select("invitationcode").eq("invitationcode",code);
                    Group codeGroup = groupMapper.selectOne(queryWrapper);
                    if(ObjectUtils.isEmpty(codeGroup)){

                        //TODO 查出年级
                        //从user里查出class 再从clazz里查出grade
                        userWrapper.select("class_id").eq("id",userId);
                        User classIdUser = userMapper.selectOne(userWrapper);
                        Long classId = classIdUser.getClassId();

                        group.setClassId(Math.toIntExact(classId));
                        group.setInvitationcode(code);
                        group.setLeader(name);
                        int insert = groupMapper.insert(group);
                        if(insert == 1){
                            //添加小组成功 更改学生的小组信息
                            queryWrapper.select("*").eq("leader_id",userId);
                            Group nowGroup = groupMapper.selectOne(queryWrapper);
                            user.setGroupId(nowGroup.getId());
                            userMapper.updateById(user);
                            return 1;
                        }
                    }


                }
            }

        }

        return 0;
    }

    @Override
    public String getCode(Long id) {
        //查出小组
        QueryWrapper<Group> queryWrappe= new QueryWrapper<>();
        queryWrappe.select("invitationcode").eq("leader_id",id);
        Group group = groupMapper.selectOne(queryWrappe);
        if(ObjectUtils.isEmpty(group)){
            return "";
        }else{
            return group.getInvitationcode();
        }
    }

    @Override
    public ResultVO getGroupInfo(Long groupID) {
        ResultVO resultVO;
        Group group = groupMapper.selectById(groupID);
        if(ObjectUtils.isEmpty(group)){
            resultVO = ResultVO.failure("获取小组信息失败！");
        }else{
            resultVO = ResultVO.success("获取小组信息成功！");
            resultVO.data("group",group);
        }
        return resultVO;
    }


    //获取工作台数据
    //已完成 未完成 已截止
    @Override
    public ResultVO getWorkBench(Long groupID) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").eq("group_id",groupID);
        List<Task> tasks = taskMapper.selectList(queryWrapper);
        queryWrapper.select("id").eq("group_id",groupID).eq("sta","wait");
        Integer Sum = tasks.size();
        List<Task> waitTasks = taskMapper.selectList(queryWrapper);
        Integer done = Sum - waitTasks.size();
        Date date = new Date();
        queryWrapper.select("group_id").eq("group_id", groupID).lt("finishedDate", date);
        List<Task> finishedTasks = taskMapper.selectList(queryWrapper);
        HashMap<String,Object> resMap = new HashMap<>();
        resMap.put("done",done);
        resMap.put("wait",Sum - done);
        resMap.put("finished",finishedTasks.size());
        ResultVO success = ResultVO.success("工作台！");
        success.setData(resMap);
        return success;
    }

    @Override
    public ResultVO getGroupByYear(Integer year) {
        ResultVO resultVO = ResultVO.success("查询成功");
        HashMap<String, Object> map = new HashMap<>();
        map.put("grade_id",year);
        map.put("is_deleted",0);
        List<Group> groups = groupMapper.selectByMap(map);
        if(ObjectUtils.isEmpty(groups)){
            return ResultVO.failure("查询失败！");
        }
        resultVO.data("groups",groups);
        return resultVO;
    }

    @Override
    public ResultVO getGroupList(Long classID) {
        ResultVO resultVO;
        HashMap<String, Object> map = new HashMap<>();
        map.put("class_ID",classID);
        map.put("is_deleted",0);
        List<Group> groups = groupMapper.selectByMap(map);

        ArrayList<Object> groupsList = new ArrayList<>();
        if(groups.isEmpty()){
            resultVO = ResultVO.failure("该班级小组为空！");
        }else{
            resultVO = ResultVO.success("获取小组列表成功!");


            for(Group group : groups){
                //查询出该小组的上次tTask的 upload=1
                //上次是否上传
                HashMap<String,Object> resGroupInfo = new HashMap<>();
                Long id = group.getId();
                QueryWrapper<Ttask> queryWrapper = new QueryWrapper<>();
                //限制1条
                queryWrapper.select("upload").eq("group_id",id).last("limit 1");
                Ttask ttask = ttaskMapper.selectOne(queryWrapper);
                String upload = "0";
                if(!ObjectUtils.isEmpty(ttask)){
                    upload = ttask.getUpload();
                }

                resGroupInfo.put("group",group);
                resGroupInfo.put("upload",upload);
                //作业未完成次数
                queryWrapper.select("upload").eq("group_id",id).eq("upload",0);
                Long num = ttaskMapper.selectCount(queryWrapper);
                resGroupInfo.put("num",num);
                groupsList.add(resGroupInfo);
            }

            HashMap<String,Object> resMap=  new HashMap<>();
            resMap.put("groups",groupsList);
            resultVO.setData(resMap);
        }
        return resultVO;
    }
}