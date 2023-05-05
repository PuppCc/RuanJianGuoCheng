package com.easyse.easyse_simple.service.tasksservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.easyse.easyse_simple.mapper.tasksservice.GroupMapper;
import com.easyse.easyse_simple.mapper.tasksservice.TaskMapper;
import com.easyse.easyse_simple.mapper.tasksservice.TtaskMapper;
import com.easyse.easyse_simple.mapper.userservice.UserMapper;

import com.easyse.easyse_simple.pojo.DO.task.*;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.tasksservice.FilesService;
import com.easyse.easyse_simple.service.tasksservice.MissionService;
import com.easyse.easyse_simple.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author ：rc
 * @date ：Created in 2022/10/18 9:33
 * @description：
 */
@Service
public class MissionServiceImpl implements MissionService {


    @Autowired
    GroupMapper groupMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TtaskMapper ttaskMapper;

    @Autowired
    FilesService filesService;


    @Override
    public ResultVO getLaterMission() {
        // TODO getLaterMission
        return null;
    }
    
    /**
     * @Description: 获取学生发布的任务详情
     * @Param: [java.lang.String] 
     * @return: java.util.HashMap<java.lang.String,java.lang.Object> 
     * @Author: lpc
     * @Date:2022/12/22 18:18
     */
    
    @Override
    public HashMap<String, Object> getTaskInfo(String id) {
        HashMap<String,Object> map = new HashMap<>();
        Task task = taskMapper.selectById(id);
        map.put("ttask",task);
        //查出文件地址
        LambdaQueryWrapper<Files> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Files::getPath).eq(Files::getTaskId,task.getId());
        Files one = filesService.getOne(lambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(one)){
            map.put("file",one.getPath());
        }else{
            map.put("file","null");
        }

        //获取仓库地址 查出url
        Long groupId = task.getGroupId();
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("git_url").eq("id",groupId);
        Group group = groupMapper.selectOne(queryWrapper);
        String gitUrl = group.getGitUrl();
        map.put("url",gitUrl);
        //图片
        map.put("pic","https://ts1.cn.mm.bing.net/th/id/R-C.18538835c5b6ec919ba8cadcf7049c95?rik=WFeycM1e9rSCxQ&riu=http%3a%2f%2fimg.mm4000.com%2ffile%2fb%2f0c%2f104e93fd05.jpg&ehk=sKIDtkqGx0XSyp%2b8PPxi1oEMMDf%2ff55RbI2EXccdQm0%3d&risl=&pid=ImgRaw&r=0");
        return map;
    }

    @Override
    public ResultVO searchTask(String str) {
        QueryWrapper<Task> queryWrapper =new QueryWrapper<>();
        queryWrapper = queryWrapper.select("*").like("task_name", str);
        List<Task> tasks = taskMapper.selectList(queryWrapper);
        ResultVO resultVO = ResultVO.success("搜索成功");
        HashMap<String,Object> resMap = new HashMap<>();
        resMap.put("tasks",tasks);
        resultVO.setData(resMap);
        return resultVO;
    }

    @Override
    public ResultVO setTaskQA(Long id, String q, String a) {
        Task task = taskMapper.selectById(id);
        if(ObjectUtils.isEmpty(task)){
            return ResultVO.failure("操作失败！");
        }
        if(StringUtils.hasText(q)){
            task.setQuestion(q);
        }
        if(StringUtils.hasText(a)){
            task.setAnswer(a);
        }
        int insert = taskMapper.insert(task);
        if(insert==1){
            return ResultVO.success("问答成功！");
        }
        return ResultVO.failure("操作失败");
    }

    @Override
    public ResultVO setTtaskQA(Long id, String q, String a) {
        Ttask ttask = ttaskMapper.selectById(id);
        if(ObjectUtils.isEmpty(ttask)){
            return ResultVO.failure("操作失败！");
        }
        if(StringUtils.hasText(q)){
            ttask.setQuestion(q);
        }
        if(StringUtils.hasText(a)){
            ttask.setAnswer(a);
        }
        int insert = ttaskMapper.insert(ttask);
        if(insert==1){
            return ResultVO.success("问答成功！");
        }
        return ResultVO.failure("操作失败");
    }

    /**
     * @Description: 获取老师发布的任务详情
     * @Param: [java.lang.String]
     * @return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Author: lpc
     * @Date:2022/12/22 18:18
     */

    @Override
    public HashMap<String, Object> getTtaskInfo(String id) {
        HashMap<String,Object> map = new HashMap<>();
        Ttask ttask = ttaskMapper.selectById(id);
        map.put("task",ttask);
        //获取仓库地址 查出url
        Long groupId = ttask.getGroupId();

        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("git_url").eq("id",groupId);
        Group group = groupMapper.selectOne(queryWrapper);
        String gitUrl = group.getGitUrl();
        map.put("url",gitUrl);
        //图片
        map.put("pic","https://ts1.cn.mm.bing.net/th/id/R-C.18538835c5b6ec919ba8cadcf7049c95?rik=WFeycM1e9rSCxQ&riu=http%3a%2f%2fimg.mm4000.com%2ffile%2fb%2f0c%2f104e93fd05.jpg&ehk=sKIDtkqGx0XSyp%2b8PPxi1oEMMDf%2ff55RbI2EXccdQm0%3d&risl=&pid=ImgRaw&r=0");
        //查出文件地址
        LambdaQueryWrapper<Files> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Files::getPath).eq(Files::getTtaskId,ttask.getId());
        Files one = filesService.getOne(lambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(one)){
            map.put("file",one.getPath());
        }else{
            map.put("file","null");
        }
        return map;
    }

    @Override
    public boolean checkMission(String id, String t) {
        if("0".equals(t)){

            Task task = taskMapper.selectById(id);
            if(ObjectUtils.isEmpty(task)){
                return false;
            }else{


                if("1".equals(task.getChecked())){
                    task.setChecked("0");
                }else if("0".equals(task.getChecked())){
                    task.setChecked("1");
                }
                int i = taskMapper.updateById(task);
                if(i==1){
                    return true;
                }
            }
        }

        if("1".equals(t)){
            Ttask ttask = ttaskMapper.selectById(id);
            if(ObjectUtils.isEmpty(ttask)){
                return false;
            }else{


                if("1".equals(ttask.getChecked())){
                    ttask.setChecked("0");
                }else if("0".equals(ttask.getChecked())){
                    ttask.setChecked("1");
                }
                int i = ttaskMapper.updateById(ttask);
                if(i==1){
                    return true;
                }
            }
        }
        return false;
    }



    @Override
    public ResultVO deleteTtask(Long id) {
        int i = ttaskMapper.deleteById(id);
        if(i==1){
            return  ResultVO.success("删除成功！");
        }
        return ResultVO.failure("删除失败！");
    }

    @Override
    public ResultVO deleteTask(Long id) {
        int i = taskMapper.deleteById(id);
        if(i==1){
            return  ResultVO.success("删除成功！");
        }
        return ResultVO.failure("删除失败！");
    }


    @Override
    public ResultVO getMissionList(Long groupID) {
        ResultVO resultVO;
        HashMap<String,Object> params = new HashMap<>();
        params.put("group_Id",groupID);
        //获取学生任务
        List<Task> tasks = taskMapper.selectByMap(params);
        //获取老师任务
        List<Ttask> ttasks = ttaskMapper.selectByMap(params);
        if(tasks.isEmpty() && ttasks.isEmpty()){
            resultVO = ResultVO.failure("任务信息为空！");
        }else{
            resultVO = ResultVO.success("获取任务列表成功！");
            HashMap<String,Object> data = new HashMap<>();
            data.put("tasks",tasks);
            data.put("ttasks",ttasks);
            resultVO.setData(data);
        }
        return resultVO;
    }

    @Override
    public ResultVO addttask(Ttask ttask) {
        //添加组长信息
        Long groupId = ttask.getGroupId();
        Group group = groupMapper.selectById(groupId);
        String leader = group.getLeader();
        ttask.setAuther(leader);
        int insert = ttaskMapper.insert(ttask);
        if(insert==1){
            return ResultVO.success("任务创建成功!");
        }
        return ResultVO.failure("创建任务失败");
    }

    @Override
    public ResultVO addMission(Task task) {
        ResultVO resultVO;
        int insert = taskMapper.insert(task);
        if(insert == 1){
            HashMap<String,Object> map = new HashMap<>();
            HashMap<String,Object> selectMap = new HashMap<>();
            selectMap.put("task_name",task.getTaskName());
            map.put("mission", taskMapper.selectByMap(selectMap));
            resultVO = ResultVO.success("任务添加成功");
            resultVO.setData(map);
            return resultVO;
        }else{
            resultVO = ResultVO.failure("发生未知错误，任务添加失败！");
        }

        return resultVO;
    }

    @Override
    public ResultVO getStudentMission(Integer userId) {
        ResultVO resultVO;
        //查出学生信息 拿到小组号和姓名
        User user = userMapper.selectById(userId);
        String username = user.getUsername();
        Long groupId = user.getGroupId();
        //通过姓名和小组号查出任务
        HashMap<String,Object> params = new HashMap<>();
        params.put("group_ID",groupId);
        params.put("assignedTO",username);
        List<Task> tasks = taskMapper.selectByMap(params);
        if(tasks.isEmpty()){
            resultVO = ResultVO.failure("任务为空！");
        }else{
            resultVO = ResultVO.success("获取任务信息成功!");
            HashMap<String,Object> data = new HashMap<>();
            data.put("missions",tasks);
            resultVO.setData(data);
        }
        return resultVO;
    }

    @Override
    public ResultVO updateMission(Task task) {
        ResultVO resultVO = null;
        Long id = task.getId();
        if(ObjectUtils.isEmpty(id)){
            resultVO = ResultVO.failure("发生错误，更新任务失败！");
        }else{
            task.setSta("done");
            int i = taskMapper.updateById(task);
            if(i == 1){
                resultVO = ResultVO.success("更新任务成功!");
            }
        }
        return resultVO;
    }

    @Override
    public ResultVO endMission(String way, Integer userID, Integer taskID, String stroy) {
        //TODO task添加任务返回的信息

        ResultVO resultVO = null;
        //任务结束 任务完成
        Integer i;
        if("finish".equals(way)){
            Task task = taskMapper.selectById(taskID);
            task.setFinisheddate(new Date());
            task.setMailto(stroy);
            String username = userMapper.selectById(userID).getUsername();
            task.setFinishedby(username);
            //更新任务
            i = taskMapper.updateById(task);
            if(i == 1){
                resultVO = ResultVO.success("任务成功结束！");
            }else{
                resultVO = ResultVO.failure("任务结束失败!");
            }

        }else if("close".equals(way)){
            //任务的时间到了 任务被关闭
            Task task = taskMapper.selectById(taskID);
            task.setCloseddate(new Date());
            String username = userMapper.selectById(userID).getUsername();
            task.setClosedby(username);
            //更新任务
            i = taskMapper.updateById(task);
            if(i == 1){
                resultVO = ResultVO.success("任务关闭成功！");
            }else{
                resultVO = ResultVO.failure("任务关闭失败!");
            }
        }else if("cancle".equals(way)){
            //任务被取消
            Task task = taskMapper.selectById(taskID);
            task.setCanceleddate(new Date());
            String username = userMapper.selectById(userID).getUsername();
            task.setCanceledby(username);
            //更新任务
            i = taskMapper.updateById(task);
            if(i == 1){
                resultVO = ResultVO.success("任务已取消！");
            }else{
                resultVO = ResultVO.failure("任务取消失败!");
            }
        }

        return resultVO;
    }


}
