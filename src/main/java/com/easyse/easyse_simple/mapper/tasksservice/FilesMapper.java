package com.easyse.easyse_simple.mapper.tasksservice;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.task.Files;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
* @author 25405
* @description 针对表【practicalTasks_files】的数据库操作Mapper
* @createDate 2022-12-08 16:07:45
* @Entity pojo.DO.systemservice.Files
*/
@Mapper
public interface FilesMapper extends BaseMapper<Files> {

    List<String> selectUrlBysCeneDesignId(Long id);

}
