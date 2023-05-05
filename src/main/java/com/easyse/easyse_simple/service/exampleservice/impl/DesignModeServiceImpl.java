package com.easyse.easyse_simple.service.exampleservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.exampleservice.DesignModeMapper;
import com.easyse.easyse_simple.pojo.DO.example.DesignMode;
import com.easyse.easyse_simple.service.exampleservice.DesignModeService;
import org.springframework.stereotype.Service;

/**
* @author 25405
* @description 针对表【examples_design_mode(设计模式种类)】的数据库操作Service实现
* @createDate 2022-12-06 19:45:13
*/
@Service
public class DesignModeServiceImpl extends ServiceImpl<DesignModeMapper, DesignMode>
implements DesignModeService{

}
