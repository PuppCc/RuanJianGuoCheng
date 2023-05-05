package com.easyse.easyse_simple.service.tasksservice.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.tasksservice.FilesMapper;
import com.easyse.easyse_simple.pojo.DO.task.Files;
import com.easyse.easyse_simple.service.tasksservice.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
* @author 25405
* @description 针对表【practicalTasks_files】的数据库操作Service实现
* @createDate 2022-12-08 16:07:45
*/
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
implements FilesService {

    @Autowired
    FilesMapper filesMapper;

    @Override
    public List<String> selectUrlBysCeneDesignId(Long id) {
        return filesMapper.selectUrlBysCeneDesignId(id);
    }
}
