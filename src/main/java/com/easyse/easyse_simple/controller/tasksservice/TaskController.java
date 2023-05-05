package com.easyse.easyse_simple.controller.tasksservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyse.easyse_simple.pojo.DO.DTO.MultiFileSD;
import com.easyse.easyse_simple.pojo.DO.example.SceneDesign;
import com.easyse.easyse_simple.pojo.DO.task.Files;
import com.easyse.easyse_simple.pojo.DO.task.Task;
import com.easyse.easyse_simple.pojo.DO.task.Ttask;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.FileUploadService;
import com.easyse.easyse_simple.service.tasksservice.FilesService;
import com.easyse.easyse_simple.service.tasksservice.MissionService;
import com.easyse.easyse_simple.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author ：rc
 * @date ：Created in 2022/10/18 8:36
 * @description：任务相关的接口
 */

@RestController
@RequestMapping(value = "/tasks/missions/")
@Api(tags = "任务相关")
public class TaskController {


    @Autowired
    FilesService filesService;

    @Autowired
    MissionService missionService;
    @Autowired
    FileUploadService fileUploadService;
    // 获取小组所有的任务列表
    @GetMapping("list/{groupID}")
    public ResultVO getMissionList(@PathVariable("groupID") Long groupID){

        return missionService.getMissionList(groupID);
    }


    // 新增任务
    /**
     * @Description:  新增任务
     * @Param: [com.easyse.easyse_simple.pojo.DO.task.Task]
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO
     * @Author: lpc
     * @Date:2022/12/24 16:22
     */

    @PostMapping("task")
    public ResultVO addMission(@RequestBody @Valid Task task){
        return missionService.addMission(task);
    }




    /**
     * @Description: 创建老师任务
     * @Param: [com.easyse.easyse_simple.pojo.DO.task.Ttask] 
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO 
     * @Author: lpc
     * @Date:2022/12/24 16:26
     */
    @ApiOperation(value = "创建老师的任务")
    @PostMapping("ttask")
    public ResultVO addTTask(@RequestBody @Valid Ttask ttask){
        return missionService.addttask(ttask);
    }

    //修改任务信息
    @RequestMapping(value = "",method = RequestMethod.PATCH)
    public ResultVO updateMission(@RequestBody Task task){
        return missionService.updateMission(task);
    }

    //获取某一学生的所有任务
    @GetMapping("student/{userId}")
    public ResultVO getStudentMission(@PathVariable("userId") Integer userId){
        return missionService.getStudentMission(userId);
    }

    //结束任务
    @PatchMapping("end/{way}/{userID}/{taskID}")
    public ResultVO endMission(@PathVariable("way")String way,
                               @PathVariable("userID") Integer userID,
                               @PathVariable("taskID") Integer taskID,
                               @RequestParam(value = "story",required = false) String stroy){
        //TODO 参数校验
        //TODO 接口测试
        //检查way的方式 不通过直接返回错误
        return missionService.endMission(way,userID,taskID,stroy);
    }

    // TODO 最近快截止的任务
    @GetMapping("later")
    public ResultVO laterMission(){
        return  missionService.getLaterMission();
    }
    
    /**
     * @Description: 获取任务详情信息
     * @Param: [java.lang.String, java.lang.String] 
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO 
     * @Author: lpc
     * @Date:2022/12/22 18:53
     */
    
    @GetMapping("info/{id}/{t}")
    public ResultVO getMissionInfo(@PathVariable String id,@PathVariable String t){
        HashMap<String,Object> map = new HashMap<>();
        if("0".equals(t)){
            //学生
            map =  missionService.getTaskInfo(id);
        }else if("1".equals(t)){
            //老师
             map = missionService.getTtaskInfo(id);
        }else {
            return ResultVO.failure("参数有误!");
        }
        ResultVO res = ResultVO.success("获取任务详情成功");
        res.setData(map);
        return res;

    }

    @PostMapping("check/{id}/{t}")
    public ResultVO checkMission(@PathVariable String id,@PathVariable String t){
        HashMap<String,Object> map = new HashMap<>();
        if("0".equals(t) || "1".equals(t)){
            //学生 或者 老师
            boolean OK = missionService.checkMission(id,t);
            if(OK){
                ResultVO res = ResultVO.success("检查成功");
                res.setData(map);
                return res;
            }

            return ResultVO.failure("审查失败!");
        }else {
            return ResultVO.failure("参数有误!");
        }

    }

    @PatchMapping("ttask/{id}")
    public ResultVO deletetTask(@PathVariable Long id){
        return missionService.deleteTtask(id);
    }


    @PatchMapping("task/{id}")
    public ResultVO deleteTask(@PathVariable Long id){
        return missionService.deleteTask(id);
    }

    @GetMapping("search/{str}")
    public ResultVO searchtask(@PathVariable String str){
        return missionService.searchTask(str);
    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件：需要id")
    public ResultVO uploadFile(@RequestParam("file") MultipartFile file,
                               @RequestParam("taskID")Long taskID){
        if (file != null) {
            String returnFileUrl = fileUploadService.upload(file, true);
            if (returnFileUrl.equals("error")) {
                return ResultVO.failure("文件上传失败，请稍后再试~");
            }

            Files build = Files.builder().TaskId(taskID)
                    .path(returnFileUrl)
                    .build();
            if(filesService.save(build)){
                ResultVO success = ResultVO.success("文件上传成功");
                success.data("returnFileUrl", returnFileUrl);
                return success;
            }else{
                return ResultVO.failure("文件上传失败，请稍后再试~");
            }
        } else {
            return ResultVO.failure("文件为空，请上传文件！");
        }
    }

    //提问
    @PostMapping("q/{id}/{t}")
    public ResultVO question(@RequestParam(value = "question",required = false)String q,
                             @RequestParam(value = "answer",required = false) String a,
                             @PathVariable String t,
                             @PathVariable Long id){
        if(!StringUtils.hasText(q)){
            q="";
        }
        if(!StringUtils.hasText(a)){
            a="";
        }
        if("0".equals(t)){
            return missionService.setTaskQA(id,q,a);
        }else if("1".equals(t)){
            return missionService.setTtaskQA(id,q,a);
        }else{
            return ResultVO.failure("参数有误！");
        }

    }




}
