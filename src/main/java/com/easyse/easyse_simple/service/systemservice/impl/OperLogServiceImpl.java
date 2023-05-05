package com.easyse.easyse_simple.service.systemservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.systemservice.OperLogMapper;
import com.easyse.easyse_simple.pojo.DO.system.OperLog;
import com.easyse.easyse_simple.service.systemservice.OperLogService;
import org.springframework.stereotype.Service;

/**
* @author 25405
* @description 针对表【system_oper_log(操作日志记录)】的数据库操作Service实现
* @createDate 2022-11-11 19:57:27
*/
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog>
    implements OperLogService {

}




