package com.easyse.easyse_simple.controller.tasksservice;

import com.easyse.easyse_simple.pojo.DO.task.Group;
import com.easyse.easyse_simple.pojo.vo.ResultVO;

import com.easyse.easyse_simple.service.tasksservice.GroupService;
import com.easyse.easyse_simple.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * @author ：rc
 * @date ：Created in 2022/10/14 11:58
 * @description：实践任务
 *
 * */

@Api(tags = "小组相关")
@RestController
@RequestMapping(value = "/tasks/")

public class GroupController {
    @Autowired
    GroupService groupService;

    @ApiOperation(value = "按班级查看小组列表")
    //查询小组列表
    @GetMapping(value = "projects/teams/{classID}")
    public ResultVO joinTeam(@PathVariable("classID") Long classID){
        return groupService.getGroupList(classID);
    }

    @ApiOperation(value = "查看全部小组列表")
    //查询小组列表
    @GetMapping(value = "projects/teams")
    public ResultVO joinTeam(){
        return groupService.getGroupAllList();
    }


    //加入小组
    @RequestMapping(value = "projects/teams/{teamCode}/{userID}",method = RequestMethod.PATCH)
    public ResultVO joinTeam(@PathVariable("teamCode") String teamCode,
                             @PathVariable("userID") Long userID){
        //TODO 参数校验
        return groupService.joinGroup(teamCode,userID);
    }
    //退出小组/踢人
    @RequestMapping(value = "projects/teams/{userID}",method = RequestMethod.PATCH)
    public ResultVO quitTeam(@PathVariable("userID") Long userID){
        //TODO 参数校验
        return groupService.quitGroup(userID);
    }

    //更新项目信息
    @RequestMapping(value = "projects/info",method = RequestMethod.PUT)
    public ResultVO updateProject(@RequestBody Group group){
        //TODO
        return groupService.updateGroupInfo(group);
    }
    //查看项目详细信息
    @GetMapping(value = "projects/info/{goupID}")
    public ResultVO getProject(@PathVariable("goupID") Long groupID){

        return groupService.getGroupInfo(groupID);
    }

    //总任务进度
    @GetMapping(value = "projects/progress/{goupID}")
    public ResultVO getProgress(@PathVariable("goupID") Long groupID){

        return groupService.getProgress(groupID);
    }

    //工作台
    @GetMapping(value = "workbench/{groupID}")
    public ResultVO getWorkBench(@PathVariable("groupID") Long groupID){

        return groupService.getWorkBench(groupID);
    }


    /**
     * @Description: 创建小组项目
     * @Param: [com.easyse.easyse_simple.pojo.DO.task.Group]
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO
     * @Author: lpc
     * @Date:2022/12/23 19:46
     */

    @PostMapping(value = "projects")
    public ResultVO addProject(@Valid @RequestBody Group group){

      Integer num =  groupService.addGroup(group);
      if(num==1){
          return ResultVO.success("创建小组成功!");
      }else{
          return ResultVO.failure("创建小组失败!");
      }
    }

    
    /**
     * @Description: 获取小组验证码
     * @Param: [java.lang.Long] 
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO 
     * @Author: lpc
     * @Date:2022/12/24 15:10
     */
    
    @GetMapping(value="projects/code/{id}")
    public ResultVO getCode(@PathVariable Long id){
        String code = groupService.getCode(id);
        if(StringUtils.hasText(code)){
            ResultVO resultVO = ResultVO.success("获取邀请码成功");
            HashMap<String,Object> map = new HashMap<>();
            map.put("code",code);
            resultVO.setData(map);
            return resultVO;
        }
        return ResultVO.failure("获取邀请码失败了。");
    }
    
    /**
     * @Description: 删除小组 
     * @Param: [java.lang.Long] 
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO 
     * @Author: lpc
     * @Date:2022/12/24 15:09
     */
    
    @PostMapping(value = "projects/delete/{id}")
    public ResultVO deleteGroup(@PathVariable Long id){
        return groupService.deleteGroup(id);
    }

    /**
     * @Description: 查询小组成员
     * @Param: [java.lang.Long]
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO
     * @Author: lpc
     * @Date:2022/12/24 15:24
     */

    @GetMapping(value = "projects/members/{id}")
    public ResultVO groupMembers(@PathVariable Long id){
        HashMap<String,Object> map = groupService.getGroupMembers(id);
        if(ObjectUtils.isEmpty(map)){
            return ResultVO.failure("查询失败！");
        }else{
            ResultVO res = ResultVO.success("查询成功");
            res.setData(map);
            return res;
        }
    }


    @GetMapping("/grade/{year}")
    @ApiOperation(value = "按年级查看所有的小组")
    public ResultVO getGroupByYear(@PathVariable Integer year){
        if(year>2018 && year < 2099){
            return groupService.getGroupByYear(year);
        }
        else{
            return ResultVO.failure("参数有误!");
        }
    }






}
