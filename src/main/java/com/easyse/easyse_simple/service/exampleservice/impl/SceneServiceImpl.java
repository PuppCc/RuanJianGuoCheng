package com.easyse.easyse_simple.service.exampleservice.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.exampleservice.SceneMapper;
import com.easyse.easyse_simple.pojo.DO.example.Scene;
import com.easyse.easyse_simple.service.exampleservice.SceneService;
import org.springframework.stereotype.Service;

/**
* @author 25405
* @description 针对表【examples_scene(场景设计方案)】的数据库操作Service实现
* @createDate 2022-12-08 15:25:20
*/
@Service
public class SceneServiceImpl extends ServiceImpl<SceneMapper, Scene>
implements SceneService {

}
