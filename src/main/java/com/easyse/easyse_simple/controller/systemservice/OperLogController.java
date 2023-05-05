package com.easyse.easyse_simple.controller.systemservice;

import com.easyse.easyse_simple.pojo.DO.system.OperLog;
import com.easyse.easyse_simple.service.systemservice.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: zky
 * @date: 2022/11/11
 * @description:
 */
//@RestController
//@RequestMapping("/system/log")
//@Api(tags = "操作日志相关") 单体项目下没啥用，不需要前端调用
public class OperLogController {

    @Autowired
    OperLogService operLogService;

    /**
     * 存储操作日志
     * @param operLog
     */
    @PostMapping("/save")
    public void saveLog(@RequestBody OperLog operLog) {
        operLogService.save(operLog);
    }

}
