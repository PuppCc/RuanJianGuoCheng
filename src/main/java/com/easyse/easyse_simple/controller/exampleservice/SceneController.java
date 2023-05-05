package com.easyse.easyse_simple.controller.exampleservice;

import com.easyse.easyse_simple.pojo.DO.example.Scene;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.exampleservice.SceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: zky
 * @date: 2022/12/08
 * @description: 技术类别下拉菜单
 */
@RequestMapping("/techshare/scene")
@RestController
@Api(tags = "技术类别下拉菜单")
public class SceneController {

    @Autowired
    SceneService sceneService;

    @GetMapping("/list")
    @ApiOperation(value = "技术类别下拉菜单")
    public ResultVO sceneList(){
        List<Scene> list = sceneService.list();
        List<HashMap<String, Object>> lists = new ArrayList<>();
        // 协调前端，添加key 和 senceName
        list.forEach(scene -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("scene", scene);
            map.put("label", scene.getSceneName());
            map.put("key", scene.getId());
            lists.add(map);
        });

        ResultVO resultVO = ResultVO.success("查询技术类别列表成功");
        resultVO.data("lists", lists);
        return resultVO;
    }
}
