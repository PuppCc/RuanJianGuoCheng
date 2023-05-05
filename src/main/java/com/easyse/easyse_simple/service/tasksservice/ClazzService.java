package com.easyse.easyse_simple.service.tasksservice;

import com.easyse.easyse_simple.pojo.DO.task.Clazz;
import com.easyse.easyse_simple.pojo.vo.ResultVO;

public interface ClazzService {
    ResultVO addClass(Clazz clas);

    ResultVO getClassList();

    ResultVO getClassListByGrade(String grade);

    ResultVO deleteClass(Long id);
}
