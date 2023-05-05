package com.easyse.easyse_simple.service.exampleservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyse.easyse_simple.pojo.DO.example.DesignModeCase;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;

/**
* @author 25405
* @description 针对表【examples_design_mode_case(设计模式案例)】的数据库操作Service
* @createDate 2022-12-07 23:06:59
*/
public interface DesignModeCaseService extends IService<DesignModeCase> {

    int addCase(DesignModeCase designModeCase);

}
