package com.easyse.easyse_simple.service;

import com.easyse.easyse_simple.pojo.DO.system.OperLog;
import com.easyse.easyse_simple.service.systemservice.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: zky
 * @date: 2022/11/11
 * @description: 异步调用日志服务
 */
@Service
public class AsyncLogService {
    @Autowired
    private OperLogService operLogService;

    /**
     * 保存系统日志记录
     */
    @Async("logthreadpool")
    public void saveSysLog(OperLog operLog) {
        operLogService.save(operLog);
    }

}
