package com.easyse.easyse_simple.mapper.systemservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.system.OperLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author 25405
* @description 针对表【system_oper_log(操作日志记录)】的数据库操作Mapper
* @createDate 2022-11-11 19:57:27
* @Entity com.easyse.servicesystem.OperLog
*/
@Mapper
@Component
public interface OperLogMapper extends BaseMapper<OperLog> {

}




