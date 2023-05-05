package com.easyse.easyse_simple.controller.exampleservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easyse.easyse_simple.pojo.DO.DTO.MultiFileSD;
import com.easyse.easyse_simple.pojo.DO.example.Scene;
import com.easyse.easyse_simple.pojo.DO.example.SceneDesign;
import com.easyse.easyse_simple.pojo.DO.task.Files;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.FileUploadService;
import com.easyse.easyse_simple.service.exampleservice.SceneDesignService;
import com.easyse.easyse_simple.service.exampleservice.SceneSceneService;
import com.easyse.easyse_simple.service.exampleservice.SceneService;
import com.easyse.easyse_simple.service.tasksservice.FilesService;
import com.easyse.easyse_simple.service.tasksservice.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author: zky
 * @date: 2022/12/08
 * @description: 技术分享-》对应数据库中的场景设计
 */
@RequestMapping("/techshare/designmode")
@RestController
@Api(tags = "技术分享")
public class TechShareController {

    @Autowired
    FilesService practicaltasksFilesService;

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 技术分享和文件关联处理业务层
     */
    @Autowired
    SceneSceneService sceneSceneService;

    @Autowired
    SceneDesignService sceneDesignService;

    @Autowired
    GroupService groupService;

    @Autowired
    SceneService sceneService;

    /**
     * 技术分享和文件一起上传
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "上传技术分享：title、groupId、sceneId、date不能为空")
    public ResultVO addTechShare(/*@RequestParam("files") MultipartFile[] files,
                                 @Validated @ModelAttribute SceneDesign sceneDesign,*/
            @ModelAttribute MultiFileSD multiFile){
        MultipartFile[] files = multiFile.getFiles();

        LambdaQueryWrapper<SceneDesign> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SceneDesign::getTitle, multiFile.getTechShare());
        SceneDesign sd = sceneDesignService.getOne(wrapper);
        if(!Objects.isNull(sd)) {
            return ResultVO.failure("该课题内容已存在，请修改课题名称~");
        }

        SceneDesign sceneDesign = SceneDesign.builder().groupId(multiFile.getGroupId())
                .title(multiFile.getTechShare())
                .sceneId(Long.parseLong(multiFile.getSceneId()))
                .shareGmt(multiFile.getDate())
                .build();


        ResultVO success = ResultVO.success();
        ArrayList<String> urls = new ArrayList<>();
        if(!Objects.isNull(files)) {
            if (!sceneDesignService.save(sceneDesign)) {
                return ResultVO.failure("技术分享信息错误~");
            }

            LambdaQueryWrapper<SceneDesign> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(SceneDesign::getTitle, multiFile.getTechShare());
            sd = sceneDesignService.getOne(wrapper1);
            for(MultipartFile file : files) {
                String returnFileUrl = fileUploadService.upload(file, true);
                urls.add(returnFileUrl);
                Files build = Files.builder().scenedesignId(sd.getId())
                        .path(returnFileUrl)
                        .build();
                if(!practicaltasksFilesService.save(build)) {
                    return ResultVO.failure("文件上传失败，请稍后再试~");
                }
            }
            success.data("urls", urls);
            return success;
        } else {
            return ResultVO.failure("文件上传不能为空~");
        }

    }

    @GetMapping("/list/{id}")
    @ApiOperation(value = "技术分享列表")
    public ResultVO techShareList(@PathVariable("id")Long id){
        List<SceneDesign> list = sceneDesignService.list(new LambdaQueryWrapper<SceneDesign>().eq(SceneDesign::getSceneId, id));
        List<HashMap<String, Object>> lists = new ArrayList<>();
        if(!Objects.isNull(list)) {
            list.forEach(sceneDesign -> {
                HashMap<String, Object> objectObjectHashMap = new HashMap<>();
                List<String> urlList = practicaltasksFilesService.selectUrlBysCeneDesignId(sceneDesign.getId());
                // TODO 待优化
                Long groupId = sceneDesign.getGroupId();
                ResultVO groupInfo = groupService.getGroupInfo(groupId);
                objectObjectHashMap.put("sceneDesign", sceneDesign);
                objectObjectHashMap.put("urlLists", urlList);
                objectObjectHashMap.put("group", groupInfo);
                lists.add(objectObjectHashMap);
            });

            ResultVO resultVO = ResultVO.success("技术分享列表查询成功！");
            resultVO.data("lists", lists);
            return resultVO;
        } else {
            return ResultVO.failure("技术分享为空，请上传技术分享~");
        }
    }

    /**
     * 技术分享的单文件
     * @param file
     * @param techShareId
     * @return
     */
    @PostMapping("/upload/{techShareId}")
    @ApiOperation(value = "上传技术分享文件：需要传递技术分享id")
    public ResultVO uploadFile(@RequestParam("file") MultipartFile file,
                               @PathVariable("techShareId")Long techShareId){
        if (file != null) {
            String returnFileUrl = fileUploadService.upload(file, true);
            if (returnFileUrl.equals("error")) {
                return ResultVO.failure("文件上传失败，请稍后再试~");
            }
            Files build = Files.builder().scenedesignId(techShareId)
                    .path(returnFileUrl)
                    .build();
            if(practicaltasksFilesService.save(build)){
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

}
