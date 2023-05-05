package com.easyse.easyse_simple.service.exampleservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.exampleservice.SceneDesignMapper;
import com.easyse.easyse_simple.pojo.DO.example.SceneDesign;
import com.easyse.easyse_simple.service.exampleservice.SceneDesignService;
import org.springframework.stereotype.Service;


/**
* @author 25405
* @description 针对表【examples_scene_design(场景设计、技术分享案例)】的数据库操作Service实现
* @createDate 2022-12-09 18:44:26
*/
@Service
public class SceneDesignServiceImpl extends ServiceImpl<SceneDesignMapper, SceneDesign>
implements SceneDesignService {

}
