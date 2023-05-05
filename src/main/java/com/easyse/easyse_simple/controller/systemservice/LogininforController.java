package com.easyse.easyse_simple.controller.systemservice;

import com.easyse.easyse_simple.pojo.DO.system.Logininfor;
import com.easyse.easyse_simple.service.systemservice.LogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: zky
 * @date: 2022/11/11
 * @description: 单体项目下没啥用，不需要前端调用
 */
//@RestController
//@RequestMapping("/system/loginlog")
//@Api(t = "登录日志相关") 单体项目下没啥用，不需要前端调用
public class LogininforController {

    @Autowired
    LogininforService logininforService;

    /**
     * 存储登录日志
     * @param logininfor 封装的登录信息
     */
    @PostMapping("/save")
    public void saveLog(@RequestBody Logininfor logininfor) {
        logininforService.save(logininfor);
    }
}
