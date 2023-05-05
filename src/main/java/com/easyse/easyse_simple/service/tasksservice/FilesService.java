package com.easyse.easyse_simple.service.tasksservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyse.easyse_simple.pojo.DO.task.Files;

import java.util.List;


/**
* @author 25405
* @description 针对表【practicalTasks_files】的数据库操作Service
* @createDate 2022-12-08 16:07:45
*/
public interface FilesService extends IService<Files> {

    List<String> selectUrlBysCeneDesignId(Long id);

}
