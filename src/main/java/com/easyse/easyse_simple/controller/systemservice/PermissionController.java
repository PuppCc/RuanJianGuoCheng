package com.easyse.easyse_simple.controller.systemservice;

import com.easyse.easyse_simple.service.systemservice.MenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zky
 * @date: 2022/10/19
 * @description:
 */
@RestController
@RequestMapping("/system/permission")
@Api(tags = "权限相关") /*单体项目下没啥用，不需要前端调用*/
public class PermissionController /**implements SystemClient*/ {

    @Autowired
    MenuService menuService;

    /**
     * 查询用户权限
     * @param id 用户id
     * @return 权限字符串
     */
    @GetMapping("/{id}")
    public List<String> selectPermsByUserId(@PathVariable("id")Long id) {
        return menuService.selectPermsByUserId(id);
    }

    /**
     * 测试
     * @return
     */
    @GetMapping("/list")
    public List<String> test(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("test");
        strings.add("123");
        return strings;
    }

}
